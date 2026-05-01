package application.service;

import application.domain.Client;
import application.service.outputs.IAuthenticable;
import application.service.outputs.IClientManagement;
import application.service.ports.IClientRepository;

public class ClientServiceImpl implements IAuthenticable, IClientManagement {
    private final IClientRepository clientRepository;
    private Client currentClient; // Is used to track the logged user

    public ClientServiceImpl(IClientRepository clientRepository) {

        this.clientRepository = clientRepository;
    }

    @Override
    public void registerClient(Client newClient) {

        // 1. Validaciones de nulidad y texto vacío (Fail-fast)
        if (newClient.getFullName() == null || newClient.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo es obligatorio.");
        }
        if (newClient.getUserName() == null || newClient.getUserName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }

        // 2. Validaciones de seguridad y formato
        if (newClient.getPassword() == null || newClient.getPassword().length() < 6) {
            throw new IllegalArgumentException("Por seguridad, la contraseña debe tener al menos 6 caracteres.");
        }

        // Usamos una expresión regular ("\\d+") para validar que la identificación contenga SOLO números
        if (newClient.getIdentification() == null || !newClient.getIdentification().matches("\\d+")) {
            throw new IllegalArgumentException("La identificación (cédula) debe contener únicamente números.");
        }

        if (newClient.getCellPhone() == null || !newClient.getCellPhone().matches("\\d+")) {
            throw new IllegalArgumentException("El número de celular debe contener únicamente números.");
        }

        // 3. Validaciones de reglas de negocio en la Base de Datos (Unicidad)
        // Verificamos si el 'userName' ya existe para evitar duplicados
        if (clientRepository.findByUserName(newClient.getUserName()).isPresent()) {
            throw new IllegalStateException("El nombre de usuario '" + newClient.getUserName() + "' ya está en uso. Por favor elija otro.");
        }

        /*
         Nota: Si en tu IClientRepository tienes un método para buscar por cédula,
         también deberías validar esto para evitar que una persona se registre dos veces:

         if (clientRepository.findByIdentification(newClient.getIdentification()).isPresent()) {
             throw new IllegalStateException("Ya existe un cliente registrado con esta identificación.");
         }
        */

        // 4. Blindaje de estados iniciales (Defensive Programming)
        // Aunque el constructor lo hace, el Servicio se asegura de que nadie
        // haya inyectado un cliente que ya venga autenticado o bloqueado por error.
        newClient.setFailedIntents(0);
        newClient.setBlocked(false);
        newClient.setAuthenticated(false);

        // 5. Persistencia (Guardar en la base de datos simulada)
        // Asumiendo que tu repositorio tiene un método save()
        clientRepository.save(newClient);
    }

    @Override
    public boolean authenticate(String username, String password) {
        Client client = clientRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: "  + username));

        if(client.isBlocked()) {
            throw new IllegalStateException("Account is blocked. Contact support.");
        }

        if(client.getPassword().equals(password)) {
            client.setFailedIntents(0);
            clientRepository.update(client);
            return true;
        } else {
            int intents = client.getFailedIntents() + 1;
            client.setFailedIntents(intents);

            if(intents >= Client.MAX_USER_INTENTS) {
                client.setBlocked(true);
            }

            clientRepository.update(client);
            return false;
        }
    }

    @Override
    public void logIn(String username, String password) {
        if(!authenticate(username, password)) {
            throw new IllegalArgumentException("Invalid credentials.");
        }

        Client client = clientRepository.findByUserName(username).get();
        client.setAuthenticated(true);
        clientRepository.update(client);
        this.currentClient = client;
    }

    @Override
    public void logOut() {
        if(currentClient == null) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        currentClient.setAuthenticated(false);
        clientRepository.update(currentClient);
        this.currentClient = null;
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        if (currentClient == null) {
            throw new IllegalStateException("Must be logged in to change password.");
        }
        if (!currentClient.getPassword().equals(oldPassword)){
            throw new IllegalArgumentException("Old password is incorrect.");
        }
        if (newPassword == null || newPassword.length() < 3) {
            throw new IllegalArgumentException("New password must be at least 3 characters long.");
        }

        currentClient.setPassword(newPassword);
        clientRepository.update(currentClient);

    }

    public Client getCurrentClient() {
        return currentClient;
    }
}
