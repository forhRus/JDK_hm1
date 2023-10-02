public class Program {
    public static void main(String[] args) {
        ServerWindow server = new ServerWindow();
        new ClientGUI(server);
    }

}