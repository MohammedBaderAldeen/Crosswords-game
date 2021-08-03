package m;


import java.io.*;
import java.util.ArrayList;

public class SaveHandler {

    private static String filename = "Game Names.bin";

    public static void setFilename(String type) {
        if(type == "Saved") {
            filename = "Game Names.bin";
        } else if (type == "Prebuilt") {
            filename = "PreBuiltGamesList";
        }
    }

    public static ArrayList<String> getGameNames() throws ClassNotFoundException, IOException {
        ObjectInputStream nameReader = null;
        ArrayList<String> names = null;
        while (true) {
            try {
                nameReader = new ObjectInputStream(new FileInputStream(filename));
                names = (ArrayList<String>) nameReader.readObject();
                break;
            } catch (FileNotFoundException e) {
                ObjectOutputStream creating = new ObjectOutputStream(new FileOutputStream(filename));
                creating.writeObject(new ArrayList<String>());
                creating.close();

            } finally {
                if (nameReader != null) {
                    try {
                        nameReader.close();
                    } catch (IOException e)
                    {
                    }
                }
            }
        }

        return names;
    }

    public static Grid getGame(String name) throws IOException, ClassNotFoundException {
        ObjectInputStream gameReader = null;
        Grid game;
        gameReader = new ObjectInputStream(new FileInputStream(name + ".crossword"));
        game = (Grid) gameReader.readObject();
        return game;
    }


    public static void save(Grid grid,String fileName) throws ClassNotFoundException, IOException {
        ArrayList<String> gameNames= new ArrayList();
        ObjectInputStream gameNameReader = null ;
        ObjectOutputStream gameNameWriter = null;

        try {
            gameNameReader = new ObjectInputStream(new FileInputStream("Game Names.bin"));
            gameNames = (ArrayList<String>) gameNameReader.readObject();

            gameNames.add(gameNames.size()+1 + "- " +fileName);

            gameNameWriter = new ObjectOutputStream(new FileOutputStream("Game Names.bin"));
            gameNameWriter.writeObject(gameNames);

        } catch (FileNotFoundException ex) {
            gameNameWriter = new ObjectOutputStream(new FileOutputStream("Game Names.bin"));
            gameNames.add(gameNames.size()+1 + "- " +fileName);
            gameNameWriter.writeObject(gameNames);

        } finally {
            try {
                if(gameNameReader!=null) {
                    gameNameReader.close();
                }
                if(gameNameWriter!=null) {
                    gameNameWriter.close();
                }
            } catch (IOException ex) { }
        }
        saveGame(grid, gameNames.size() + "- " + fileName);
    }

    private static void saveGame(Grid grid, String fileName) throws IOException {
        ObjectOutputStream gameWriter = null;
        try {
            gameWriter = new ObjectOutputStream(new FileOutputStream(fileName + ".crossword"));
            gameWriter.writeObject(grid);
        } finally {
            if(gameWriter != null) {
                gameWriter.close();
            }
        }
    }
}

