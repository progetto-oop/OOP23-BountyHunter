package buontyhunter.core;

import buontyhunter.input.*;
import buontyhunter.model.*;
import buontyhunter.physics.*;
import buontyhunter.common.*;
import buontyhunter.graphics.*;
import java.util.*;

/* this class has methods to create all gameObjects */
public class GameFactory {

    static private GameFactory instance;

    static public GameFactory getInstance() {
        if (instance == null) {
            instance = new GameFactory();
        }
        return instance;
    }

    /**
     * Create a new player
     * 
     * @param point     start position for the player
     * @param vector    current player velocity
     * @param health    current health that the player will have
     * @param maxHealth maximum health that the player can have
     * @return the player entity created
     */
    public PlayerEntity createPlayer(Point2d point, Vector2d vector, int health, int maxHealth) {
        return new PlayerEntity(GameObjectType.Player, point, vector,
                new RectBoundingBox(new Point2d(0, 0), 1, 1),
                new PlayerInputController(), new PlayerGraphicsComponent(), new PlayerPhysicsComponent(),
                health, maxHealth);
    }

    /**
     * Create a new tile manager; this object will be used to manage the tiles in
     * the game
     * 
     * @return the tile manager created
     */
    public TileManager createTileManager() {
        return new TileManager(GameObjectType.TileManager,
                new Point2d(-(GameEngine.WORLD_WIDTH / 2), GameEngine.WORLD_HEIGHT / 2), new Vector2d(0, 0),
                new RectBoundingBox(new Point2d(0, 0), GameEngine.WORLD_HEIGHT, GameEngine.WORLD_WIDTH),
                new NullInputComponent(), new MapGraphicsComponent(), new NullPhysicsComponent());
    }

    public TileManager creaTileManagerForHub() {
        return new TileManager(GameObjectType.TileManager,
                new Point2d(-(GameEngine.HUB_WIDTH / 2), GameEngine.HUB_HEIGHT / 2), new Vector2d(0, 0),
                new RectBoundingBox(new Point2d(0, 0), GameEngine.HUB_HEIGHT, GameEngine.HUB_WIDTH),
                new NullInputComponent(), new MapGraphicsComponent(), new NullPhysicsComponent());
    }

    /**
     * Create a new minimap; this object will be used to show the minimap in the
     * game when pressing the M key
     * 
     * @return the minimap created
     */
    public HidableObject createMinimap() {
        return new HidableObject(GameObjectType.MiniMap,
                new Point2d(0, 0), new Vector2d(0, 0),
                new RectBoundingBox(new Point2d(0, 0), GameEngine.WORLD_HEIGHT, GameEngine.WORLD_WIDTH),
                new MiniMapInputController(), new MiniMapGraphicsComponent(), new NullPhysicsComponent(), false);
    }

    /**
     * Create a new navigator line; this object will be used to show the navigator
     * line in the game when pressing the N key
     * 
     * @param world the world where the navigator line will be used
     * @return the navigator line created
     */
    public NavigatorLine createNavigatorLine(World world) {
        return new NavigatorLine(GameObjectType.NavigatorLine,
                new Point2d(0, 0), new Vector2d(0, 0),
                new RectBoundingBox(new Point2d(0, 0), GameEngine.WORLD_HEIGHT, GameEngine.WORLD_WIDTH),
                new NullInputComponent(), new NavigatorLineGraphicsComponent(), new NullPhysicsComponent(), world);
    }

    public FighterEntity createEnemy(Point2d point, Vector2d vector, int health, int maxHealth) {
        return new FighterEntity(GameObjectType.Enemy, point, vector,
                new RectBoundingBox(new Point2d(0, 0), 1, 1),
                new NullInputComponent(), new PlayerGraphicsComponent(), new NullPhysicsComponent(),
                health, maxHealth);
    }

    /**
     * Create a new health bar; this object will be used to show the health bar in
     * the game
     * 
     * @return the health bar created
     */
    public HealthBar createHealthBar() {
        return new HealthBar(GameObjectType.HealthBar,
                new Point2d((GameEngine.WORLD_WIDTH / 2) * (GameEngine.WINDOW_WIDTH / GameEngine.WORLD_WIDTH) - 100,
                        GameEngine.WORLD_HEIGHT * (GameEngine.WINDOW_WIDTH / GameEngine.WORLD_WIDTH) - 100),
                new Vector2d(0, 0),
                new RectBoundingBox(new Point2d(0, 0), GameEngine.WORLD_HEIGHT, GameEngine.WORLD_WIDTH),
                new NullInputComponent(), new HealthBarGraphicsComponent(), new NullPhysicsComponent());
    }

    public Teleporter createTeleporterToHub() {
        return new Teleporter(GameObjectType.Teleporter,
                Teleporter.OPEN_WORLD_TELEPORT_POS,
                new Vector2d(0, 0),
                new RectBoundingBox(new Point2d(Teleporter.OPEN_WORLD_TELEPORT_POS.x - 0.3,
                        Teleporter.OPEN_WORLD_TELEPORT_POS.y - 0.3), 1, 1),
                new NullInputComponent(), new TeleporterGraphicComponent(), new TeleporterPhysicsComponent(),
                DestinationOfTeleporterType.HUB);
    }

    public Teleporter createTeleporterToOpenWorld() {
        return new Teleporter(GameObjectType.Teleporter,
                Teleporter.HUB_TELEPORT_POS,
                new Vector2d(0, 0),
                new RectBoundingBox(
                        new Point2d(Teleporter.HUB_TELEPORT_POS.x - 0.3, Teleporter.HUB_TELEPORT_POS.y - 0.3), 1, 1),
                new NullInputComponent(), new TeleporterGraphicComponent(), new TeleporterPhysicsComponent(),
                DestinationOfTeleporterType.OpenWorld);
    }

    public InterractableArea createQuestPannelForHub(Point2d pos) {
        QuestPannel panel = new QuestPannel(GameObjectType.HidableObject,
                new Point2d(0, 0), new Vector2d(0, 0),
                new RectBoundingBox(new Point2d(0, 0), GameEngine.HUB_WINDOW_WIDTH, GameEngine.HUB_WINDOW_HEIGHT),
                new NullInputComponent(), new QuestPanelGraphicsComponent(), new NullPhysicsComponent(), false);

        return new InterractableArea(GameObjectType.InterractableArea,
                pos, new Vector2d(0, 0),
                new RectBoundingBox(pos, 3,4),
                panel);
    }

    public List<Quest> createQuests() {
        List<Quest> quests = new ArrayList<Quest>();
        quests.add(new QuestEntity("prova1","descrizione", 10));
        quests.add(new QuestEntity("prova2","descrizione", 10));
        quests.add(new QuestEntity("prova3","descrizione", 10));
        return quests;
    }

    public HidableObject createQuestJournal() {
        return new HidableObject(GameObjectType.HidableObject,
                new Point2d(0, 0), new Vector2d(0, 0),
                new RectBoundingBox(new Point2d(0, 0), GameEngine.WORLD_HEIGHT, GameEngine.WORLD_WIDTH),
                new QuestJournalInputComponent(), new QuestJournalGraphicsComponent(), new NullPhysicsComponent(), false);
    }
}
