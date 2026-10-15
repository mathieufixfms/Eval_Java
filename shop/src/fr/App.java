package fr;

import fr.business.ShopService;
import fr.business.ShopServiceImpl;
import fr.dao.ClientDao;
import fr.dao.ClientDaoImpl;
import fr.dao.FormationDao;
import fr.dao.FormationDaoImpl;
import fr.dao.UserConnectedDao;
import fr.dao.UserConnectedDaoImpl;
import fr.entities.Formation;
import fr.entities.Order;
import fr.entities.UserConnected;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        ClientDao clientDao = new ClientDaoImpl();
        UserConnectedDao userConnectedDao = new UserConnectedDaoImpl();
        FormationDao formationDao = new FormationDaoImpl();
        ShopService service = new ShopServiceImpl(clientDao, userConnectedDao, formationDao);

        try (Scanner scanner = new Scanner(System.in)) {
            runMenu(scanner, service);
        } finally {
            service.closeConnection();
        }
    }

    private static void runMenu(Scanner scanner, ShopService service) {
        Map<Long, CartItem> cart = new LinkedHashMap<>();
        UserConnected connectedUser = null;

        while (true) {
            System.out.println("\n=== SHOP FORMATIONS ===");
            System.out.println("1. Liste des formations");
            System.out.println("2. Rechercher une formation par mot-cle");
            System.out.println("3. Panier (" + cart.size() + " formation(s))");
            System.out.println("4. " + (connectedUser == null ? "Se connecter" : "Se deconnecter"));
            if (connectedUser != null) {
                System.out.println("5. Mes commandes");
            }
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            if (!scanner.hasNextLine()) {
                return;
            }

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    listFormations(scanner, service.listFormations(), cart);
                    break;
                case "2":
                    searchFormations(scanner, service, cart);
                    break;
                case "3":
                    connectedUser = showCart(scanner, cart, service, connectedUser);
                    break;
                case "4":
                    connectedUser = connectedUser == null
                        ? connect(scanner, service)
                        : disconnect(connectedUser);
                    break;
                case "5":
                    if (connectedUser == null) {
                        System.out.println("Connectez-vous pour consulter vos commandes.");
                    } else {
                        printOrders(service.listOrders(connectedUser));
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    private static void listFormations(Scanner scanner, List<Formation> formations,
                                       Map<Long, CartItem> cart) {
        if (formations.isEmpty()) {
            System.out.println("Aucune formation disponible.");
            return;
        }

        printFormations(formations);
        addFormationToCart(scanner, formations, cart);
    }

    private static void searchFormations(Scanner scanner, ShopService service,
                                         Map<Long, CartItem> cart) {
        System.out.print("Mot-cle : ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String keyword = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
        if (keyword.isEmpty()) {
            System.out.println("La recherche ne peut pas etre vide.");
            return;
        }

        List<Formation> matches = new ArrayList<>();
        for (Formation formation : service.listFormations()) {
            String name = formation.getName();
            String description = formation.getDescription();
            if ((name != null && name.toLowerCase(Locale.ROOT).contains(keyword))
                    || (description != null
                        && description.toLowerCase(Locale.ROOT).contains(keyword))) {
                matches.add(formation);
            }
        }

        if (matches.isEmpty()) {
            System.out.println("Aucune formation ne correspond a \"" + keyword + "\".");
            return;
        }

        printFormations(matches);
        addFormationToCart(scanner, matches, cart);
    }

    private static void printFormations(List<Formation> formations) {
        System.out.println("\n--- Formations disponibles ---");
        for (Formation formation : formations) {
            System.out.println(formation);
        }
    }

    private static void addFormationToCart(Scanner scanner, List<Formation> availableFormations,
                                           Map<Long, CartItem> cart) {
        System.out.print("ID a ajouter au panier (0 pour revenir) : ");
        if (!scanner.hasNextLine()) {
            return;
        }

        long formationId = parseId(scanner.nextLine());
        if (formationId == 0) {
            return;
        }

        for (Formation formation : availableFormations) {
            if (formation.getId() == formationId) {
                System.out.print("Quantite : ");
                if (!scanner.hasNextLine()) {
                    return;
                }
                int quantity = parsePositiveInt(scanner.nextLine());
                if (quantity <= 0) {
                    System.out.println("La quantite doit etre un nombre entier positif.");
                    return;
                }

                CartItem existingItem = cart.get(formationId);
                if (existingItem == null) {
                    cart.put(formationId, new CartItem(formation, quantity));
                } else if (quantity > Integer.MAX_VALUE - existingItem.quantity) {
                    System.out.println("La quantite depasse la limite autorisee.");
                    return;
                } else {
                    existingItem.quantity += quantity;
                }
                System.out.println(formation.getName() + " ajoutee au panier.");
                return;
            }
        }
        System.out.println("ID de formation invalide.");
    }

    private static UserConnected showCart(Scanner scanner, Map<Long, CartItem> cart,
                                          ShopService service, UserConnected connectedUser) {
        if (cart.isEmpty()) {
            System.out.println("Votre panier est vide.");
            return connectedUser;
        }

        while (!cart.isEmpty()) {
            List<CartItem> items = new ArrayList<>(cart.values());
            printCart(items);
            System.out.println("1. Modifier une quantite");
            System.out.println("2. Retirer une formation");
            System.out.println("3. Passer commande");
            System.out.println("0. Retour");
            System.out.print("Votre choix : ");
            if (!scanner.hasNextLine()) {
                return connectedUser;
            }

            switch (scanner.nextLine().trim()) {
                case "1":
                    changeQuantity(scanner, items);
                    break;
                case "2":
                    removeFromCart(scanner, cart, items);
                    break;
                case "3":
                    if (connectedUser == null) {
                        System.out.print("Vous devez vous connecter pour commander. "
                            + "Voulez-vous vous connecter maintenant ? (o/n) : ");
                        if (!scanner.hasNextLine()) {
                            return null;
                        }
                        String answer = scanner.nextLine().trim();
                        if (!answer.equalsIgnoreCase("o") && !answer.equalsIgnoreCase("oui")) {
                            System.out.println("Commande annulee. Votre panier est conserve.");
                            break;
                        }
                        connectedUser = connect(scanner, service);
                        if (connectedUser == null) {
                            System.out.println("Connexion echouee. Votre panier est conserve.");
                            break;
                        }
                    }
                    placeOrder(cart, service, connectedUser);
                    return connectedUser;
                case "0":
                    return connectedUser;
                default:
                    System.out.println("Choix invalide.");
            }
        }
        System.out.println("Votre panier est vide.");
        return connectedUser;
    }

    private static void printCart(List<CartItem> items) {
        System.out.println("\n--- Votre panier ---");
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            double subtotal = item.formation.getPrice() * item.quantity;
            System.out.printf(Locale.ROOT, "%d. %s x %d - %.2f EUR%n",
                i + 1, item.formation.getName(), item.quantity, subtotal);
            total += subtotal;
        }
        System.out.printf(Locale.ROOT, "Total : %.2f EUR%n", total);
    }

    private static void changeQuantity(Scanner scanner, List<CartItem> items) {
        System.out.print("Numero de la formation : ");
        if (!scanner.hasNextLine()) {
            return;
        }
        int itemNumber = parsePositiveInt(scanner.nextLine());
        if (itemNumber < 1 || itemNumber > items.size()) {
            System.out.println("Numero invalide.");
            return;
        }

        System.out.print("Nouvelle quantite : ");
        if (!scanner.hasNextLine()) {
            return;
        }
        int quantity = parsePositiveInt(scanner.nextLine());
        if (quantity <= 0) {
            System.out.println("La quantite doit etre un nombre entier positif.");
            return;
        }
        items.get(itemNumber - 1).quantity = quantity;
        System.out.println("Quantite mise a jour.");
    }

    private static void removeFromCart(Scanner scanner, Map<Long, CartItem> cart,
                                       List<CartItem> items) {
        System.out.print("Numero de la formation a retirer : ");
        if (!scanner.hasNextLine()) {
            return;
        }
        int itemNumber = parsePositiveInt(scanner.nextLine());
        if (itemNumber < 1 || itemNumber > items.size()) {
            System.out.println("Numero invalide.");
            return;
        }
        CartItem removed = items.get(itemNumber - 1);
        cart.remove(removed.formation.getId());
        System.out.println(removed.formation.getName() + " a ete retiree du panier.");
    }

    private static void placeOrder(Map<Long, CartItem> cart, ShopService service,
                                   UserConnected connectedUser) {
        Map<Formation, Integer> orderItems = new LinkedHashMap<>();
        for (CartItem item : cart.values()) {
            orderItems.put(item.formation, item.quantity);
        }
        try {
            String commandId = service.placeOrder(connectedUser, orderItems);
            cart.clear();
            System.out.println("Commande passee avec succes. Numero : " + commandId);
        } catch (IllegalStateException e) {
            System.err.println("Impossible de passer la commande : " + e.getMessage());
        }
    }

    private static void printOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("Vous n'avez pas encore passe de commande.");
            return;
        }

        Map<String, List<Order>> ordersByCommand = new LinkedHashMap<>();
        for (Order order : orders) {
            ordersByCommand.computeIfAbsent(
                order.getCommand().getId(), key -> new ArrayList<>()).add(order);
        }

        System.out.println("\n--- Mes commandes ---");
        for (Map.Entry<String, List<Order>> entry : ordersByCommand.entrySet()) {
            List<Order> commandItems = entry.getValue();
            System.out.println("\nCommande " + entry.getKey() + " - "
                + commandItems.get(0).getCommand().getDate_command());
            double total = 0;
            for (Order order : commandItems) {
                double subtotal = order.getFormation().getPrice() * order.getQuantity();
                System.out.printf(Locale.ROOT, "  %s x %d - %.2f EUR%n",
                    order.getFormation().getName(), order.getQuantity(), subtotal);
                total += subtotal;
            }
            System.out.printf(Locale.ROOT, "  Total : %.2f EUR%n", total);
        }
    }

    private static UserConnected connect(Scanner scanner, ShopService service) {
        System.out.print("Identifiant : ");
        if (!scanner.hasNextLine()) {
            return null;
        }
        String login = scanner.nextLine().trim();

        System.out.print("Mot de passe : ");
        if (!scanner.hasNextLine()) {
            return null;
        }
        String password = scanner.nextLine();

        for (UserConnected user : service.listUserConnected()) {
            if (user.getLogin() != null && user.getLogin().equals(login)
                    && user.getPasseword() != null && user.getPasseword().equals(password)) {
                System.out.println("Connexion reussie. Bonjour " + user.getLogin() + ".");
                return user;
            }
        }

        System.out.println("Identifiant ou mot de passe incorrect.");
        return null;
    }

    private static UserConnected disconnect(UserConnected user) {
        System.out.println("Deconnexion de " + user.getLogin() + ".");
        return null;
    }

    private static long parseId(String value) {
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static int parsePositiveInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static final class CartItem {
        private final Formation formation;
        private int quantity;

        private CartItem(Formation formation, int quantity) {
            this.formation = formation;
            this.quantity = quantity;
        }
    }
}