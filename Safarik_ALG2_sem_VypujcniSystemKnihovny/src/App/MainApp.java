package App;

import Utils.MailSender;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;

/**
 *
 * @author David
 */
public class MainApp {
    private BookInventory booksInv = new BookInventory();
    private ClientsList clients = new ClientsList();
    
    public void loadAllData(String pathToBookInventory, String pathToClientsList) throws IOException{
        booksInv.loadData(pathToBookInventory);
        clients.loadData(pathToClientsList);
    }
    
    public void saveAllData(String pathToBookInventory, String pathToClientsList) throws IOException{
        booksInv.saveData(pathToBookInventory);
        clients.saveData(pathToClientsList);
    }
    
    /**
     * Přídá knihu do knihovního seznamu.
     * @param name Název knihy
     * @param authorName Jméno autora
     * @param ISBN ISBN knihy
     * @param publishDate Datum vydání knihy
     * @throws ParseException 
     */
    public void addNewBookToInventory(String name, String authorName, long ISBN, String publishDate) throws ParseException{
        booksInv.addBook(new Book(name, authorName, ISBN, publishDate));
    }
    
    /**
     * Přidá knihu do knihovního seznamu.
     * @param book Kniha k přidání
     */
    public void addNewBookToInventory(Book book){
        booksInv.addBook(book);
    }
    
    /**
     * Přidá nového klienta do seznamu.
     * @param firstName Křestní jméno
     * @param lastName Příjmení
     * @param ID přidělené ID
     * @param email E-mail klienta
     */
    public void addNewClientToList(String firstName, String lastName, int ID, String email){
        clients.addClient(new Client(firstName, lastName, ID, email));
    }
    
    /**
     * Přidá nového klienta do seznamu.
     * @param client Klient k přidání
     */
    public void addNewClientToList(Client client){
        clients.addClient(client);
    }
    
    /**
     * Najde klienta s nejvyšší hodnotou ID a vrátí hodnotu o jedna vyšší.
     * @return nejvyšší ID + 1
     */
    public int getNewClientID(){
        int ID = 0;
        for(Client c : clients.getClientList()){
            if(c.getID() > ID) ID = c.getID();
        }
        return ID + 1;
    }
    
    /**
     * Vypůjčí klientovi se zadaným ID knihu podle zadaného ISBN. Vypůjčené knize přidělí dnešní datum jako datum výpůjčky.
     * @param clientID ID klienta
     * @param ISBN ISBN vypůjčované knihy
     * @return 0 pokud vše proběhlo v pořádku, 1 pokud klient nebo kniha nejsou v seznamu, 2 pokud klient není v seznamu
     * @throws ParseException 
     */
    public int rentBook(int clientID, long ISBN) throws ParseException{
        Book b = booksInv.getBookByISBN(ISBN);
        //zkontroluje, zda je kniha v inventari
        if(b == null){
            return 1;
        }
        //zmeni stav knihy v inventari na vypujceno
        if(!booksInv.rentOutBook(ISBN)){
            return 1;
        }
        
        //zkontroluje jestli je klient v seznamu
        if(clients.isIDRegistered(clientID) == false){
            return 2;
        }
        

        //zjisti dnesni datum
        LocalDate today = LocalDate.now();
        String todayString = String.format("%s/%s/%s", today.getDayOfMonth(), today.getMonthValue(), today.getYear());
        System.out.println(todayString);

        //prida knihu do klientova seznamu
        RentedBook rentedBook = new RentedBook(b.getName(), b.getAuthorName(), b.getISBN(), b.getPublishDateString(), todayString);
        clients.rentBook(clientID, rentedBook);
        
        return 0;
    }
    
    /**
     * Vrátí knihu podle zadaného ISBN vypůjčenou klientem se zadaným ID.
     * @param clientID ID klienta vracejícího knihu
     * @param ISBN ISBN vracené knihy
     * @return 0 pokud se vše podařilo, 1 pokud není klient registrovaný, nebo nemá knihu půjčenou, 2 pokud kniha nebyla v inventáři knih
     */
    public int returnBook(int clientID, long ISBN){
        if(clients.returnBook(clientID, ISBN)){
            if(booksInv.returnBook(ISBN)) return 0;
            return 2;
        }
        return 1;
    }
    
    /**
     * Metoda k posílání emailu.
     * @param to Komu
     * @param from Z jaké adresy
     * @param pass Heslo
     * @param subject Předmět zprávy
     * @param text Text zprávy
     * @throws Exception Pokud nastane jakákoliv chyba při posílání emailu
     */
    public void sendMail(String to, String from, String pass, String subject, String text) throws Exception{
        MailSender.sendMail(to, from, pass, subject, text);
    }
    
    public void sortInventoryByNameOfBook(){
        booksInv.sortByName();
    }
    
    public void sortInventoryByNameOfAuthor(){
        booksInv.sortByAuthorName();
    }
    
    public void sortInventoryByPublishDate(){
        booksInv.sortByPublishDate();
    }
    
    public void sortClientsListByLastName(){
        clients.sortByLastName();
    }
    
    public void sortClientsListByID(){
        clients.sortByID();
    }
    
    public ClientsList getClientsList(){
        return clients;
    }

    /**
     * Formátovaný výstup s informacemi o knihovně.
     * @return String formatted
     */
    public String getBookInventoryInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append("-----------------------------------------------------------------------------------------------------------\n");
        sb.append(String.format("| %-15s | %-30s | %-20s | %-14s | %-12s |\n", "K vypůjčení", "Název", "Jméno autora", "ISBN", "Datum vydáni"));
        sb.append("-----------------------------------------------------------------------------------------------------------\n");
        for(Book b : booksInv.getBooksList()){
            String s = String.format("| %-15s | %-30s | %-20s | %-14d | %-12s |", b.isIsRented()?"Ne":"Ano", b.getName(), b.getAuthorName(), b.getISBN(), b.getPublishDateString());
            sb.append(s);
            sb.append("\n");
        }
        sb.append("-----------------------------------------------------------------------------------------------------------\n");
        return sb.toString();
    }
    
    /**
     * Formátovaný výstup o registrovaných klientech.
     * @return String formatted
     */
    public String getClientsListInfo(){
        StringBuilder sb = new StringBuilder();
        for(Client c : clients.getClientList()){
            sb.append("------------------------------------------------------------------------------------------------\n");
            sb.append(String.format("| ID: %-4d | Jméno: %-10s | Příjmení: %-10s | E-mail: %-30s |\n", c.getID(), c.getFirstName(), c.getLastName(), c.getEmail()));
            sb.append("------------------------------------------------------------------------------------------------\n");
            if(c.getRentedBooks().size() == 0) sb.append("\n");
            if(c.getRentedBooks().size() > 0){
                sb.append("Vypůjčené knihy:\n");
                sb.append("--------------------------------------------------------------------------------------------\n");
                sb.append(String.format("| %-30s | %-20s | %-14s | %-15s |\n", "Název", "Jméno autora", "ISBN", "Datum vypůjčení"));
                sb.append("--------------------------------------------------------------------------------------------\n");
                for(RentedBook b : c.getRentedBooks()){
                    sb.append(String.format("| %-30s | %-20s | %-14d | %-15s |\n", b.getName(), b.getAuthorName(), b.getISBN(), b.getRentDateString()));
                }
                sb.append("--------------------------------------------------------------------------------------------\n\n");
            }
        }
        return sb.toString();
    }
    
    public static void main(String[] args) throws IOException{
        MainApp mApp = new MainApp();
        mApp.loadAllData("data/BooksInventory.txt", "data/ClientsListCopy.txt");
        System.out.println(mApp.getBookInventoryInfo());
        System.out.println(mApp.getClientsListInfo());
    }

    /**
     * Najde klienta podle zadaného ID a vrátí jeho email.
     * @param ID ID klienta.
     * @return String s emailovou adresou, nebo null, pokud není klient s daným ID
     */
    public String getClientsMail(int ID) {
        for(Client c : clients.getClientList()){
            if(c.getID() == ID){
                return c.getEmail();
            }
        }
        return null;
    }
    
}
