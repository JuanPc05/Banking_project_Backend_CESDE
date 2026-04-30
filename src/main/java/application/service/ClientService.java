package application.service;

import application.domain.Client;
import application.domain.CreditCard;
import application.service.outputs.IAuthenticable;
import application.service.ports.CreditCardRepositoryPort;
import application.service.ports.IClientRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientService implements IAuthenticable {
    private final IClientRepository clientRepository;
    private final CreditCardRepositoryPort creditCardRepositoryPort;
    private Client currentClient;
    private final Map<Integer, CreditCard> assignedCards = new HashMap<>();

    public ClientService(IClientRepository clientRepository, CreditCardRepositoryPort creditCardRepositoryPort) {
        this.clientRepository = clientRepository;
        this.creditCardRepositoryPort = creditCardRepositoryPort;
    }

    // ---------------- Métodos originales (no tocados) ----------------
    @Override
    public boolean authenticate(String username, String password) {
        Client client = clientRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        if (client.isBlocked()) {
            throw new IllegalStateException("Account is blocked. Contact support.");
        }

        if (client.getPassword().equals(password)) {
            client.setFailedIntents(0);
            clientRepository.update(client);
            return true;
        } else {
            int intents = client.getFailedIntents() + 1;
            client.setFailedIntents(intents);

            if (intents >= Client.MAX_USER_INTENTS) {
                client.setBlocked(true);
            }

            clientRepository.update(client);
            return false;
        }
    }

    @Override
    public void logIn(String username, String password) {
        if (!authenticate(username, password)) {
            throw new IllegalArgumentException("Invalid credentials.");
        }

        Client client = clientRepository.findByUserName(username).get();
        client.setAuthenticated(true);
        clientRepository.update(client);
        this.currentClient = client;
    }

    @Override
    public void logOut() {
        if (currentClient == null) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        currentClient.setAuthenticated(false);
        clientRepository.update(currentClient);
        this.currentClient = null;
    }

    // ---------------- Métodos nuevos (añadidos) ----------------

    // Asignar tarjeta a un cliente
    public void assignCardToClient(int clientId, String cardNumber) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        CreditCard card = creditCardRepositoryPort.findByCardNumber(cardNumber);
        if (card == null) {
            throw new IllegalArgumentException("Tarjeta no encontrada");
        }

        assignedCards.put(clientId, card);
    }

    // Obtener las tarjetas asignadas
    public Map<Integer, CreditCard> getAssignedCards() {
        return assignedCards;
    }

    // Listar todas las tarjetas disponibles en el repositorio
    public List<CreditCard> getAvailableCards() {
        return creditCardRepositoryPort.findAll();
    }

    // Implementación del método faltante de IAuthenticable
    @Override
    public void changePassword(String username, String newPassword) {
        Client client = clientRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + username));

        if (!client.isAuthenticated()) {
            throw new IllegalStateException("El cliente debe estar autenticado para cambiar la contraseña.");
        }

        client.setPassword(newPassword);
        clientRepository.update(client);
    }
}
