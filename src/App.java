public class App {

    public static void main(String[] args) {
        Window window = new Window("Neuro Net Simulator by Jetstream");
        window.setVisible(true);

        new Controller(window).init();
    }

}
