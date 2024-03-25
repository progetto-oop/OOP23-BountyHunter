package buontyhunter.model;

import java.util.List;

import java.util.Optional;

import buontyhunter.common.Point2d;
import buontyhunter.core.GameFactory;
import buontyhunter.core.WinnerType;
import buontyhunter.model.AI.enemySpawner.EnemyConfiguration;
import buontyhunter.model.AI.enemySpawner.EnemyRegistry;
import buontyhunter.model.AI.enemySpawner.EnemyRegistryImpl;
import buontyhunter.model.event.ChangeWorldEvent;
import buontyhunter.model.event.GameOverEvent;
import buontyhunter.physics.BoundaryCollision;

import java.util.ArrayList;

public class World {
    private GameObject player;
    private TileManager tileManager;
    private RectBoundingBox mainBBox;
    private WorldEventListener evListener;
    private HidableObject miniMap;
    private NavigatorLine navigatorLine;
    private HealthBar healthBar;
    private Teleporter tp;
    private HidableObject questJournal;
    private List<InterractableArea> interractableAreas;
    private EnemyRegistry enemyRegistry;
    private LoadingBar loadingBar;
    private WizardBossEntity wizardBoss;
    private InventoryObject inventory;

    public World(RectBoundingBox bbox) {
        mainBBox = bbox;
        this.healthBar = GameFactory.getInstance().createHealthBar();
        this.interractableAreas = new ArrayList<InterractableArea>();
        enemyRegistry = new EnemyRegistryImpl();
    }

    /**
     * get the wizard boss of the world
     * 
     * @return the wizard boss of the world
     */
    public WizardBossEntity getWizardBoss() {
        return wizardBoss;
    }

    /**
     * set the wizard boss of the world
     * 
     * @param wizardBoss the new wizard boss
     */
    public void setWizardBoss(WizardBossEntity wizardBoss) {
        this.wizardBoss = wizardBoss;
    }

    /**
     * set the loading bar of the world
     * 
     * @param loadingBar the new loading bar
     */
    public void setLoadingBar(LoadingBar loadingBar) {
        this.loadingBar = loadingBar;
    }

    /**
     * set the inventory of the world
     * 
     * @param inventory the new inventory
     */
    public void setInventory(InventoryObject inventory) {
        this.inventory = inventory;
    }

    /**
     * get the inventory of the world
     * 
     * @return the inventory of the world
     */
    public InventoryObject getInventory() {
        return inventory;
    }

    /**
     * get the loading bar of the world
     * 
     * @return the loading bar of the world
     */
    public LoadingBar getLoadingBar() {
        return loadingBar;
    }

    /**
     * set the event listener of the world
     * 
     * @param l the new event listener
     */
    public void setEventListener(WorldEventListener l) {
        evListener = l;
    }

    /**
     * set the health bar of the world
     * 
     * @param healthBar the new health bar of the world
     */
    public void setHealthBar(HealthBar healthBar) {
        this.healthBar = healthBar;
    }

    /**
     * set the tile manager of the world
     * 
     * @param tileManager the new tile manager
     * @param settedMap   the id of the map to load in the tile manager
     */
    public void setTileManager(TileManager tileManager, int settedMap) {
        this.tileManager = tileManager;
        laodMap(settedMap);
    }

    /**
     * set the teleporter of the world
     * 
     * @param tp the new teleporter
     */
    public void setTeleporter(Teleporter tp) {
        this.tp = tp;
    }

    /**
     * set the player of the world
     * 
     * @param player the new player
     */
    public void setPlayer(GameObject player) {
        this.player = player;
    }

    /**
     * set the mini map of the world
     * 
     * @param miniMap the new mini map
     */
    public void setMiniMap(HidableObject miniMap) {
        this.miniMap = miniMap;
    }

    /**
     * set the navigator line of the world
     * 
     * @param navigatorLine the new navigator line
     */
    public void setNavigatorLine(NavigatorLine navigatorLine) {
        this.navigatorLine = navigatorLine;
    }

    /**
     * add an interractable area to the world (ex: quest pannel, blacksmith, etc...)
     * 
     * @param area the new interractable area
     */
    public void addInterractableArea(InterractableArea area) {
        interractableAreas.add(area);
    }

    /**
     * get the interractable areas of the world
     * 
     * @return the interractable areas of the world
     */
    public List<InterractableArea> getInterractableAreas() {
        return interractableAreas;
    }

    /**
     * get all the enemies of the world
     * 
     * @return a list of all the enemies of the world
     */
    public List<EnemyEntity> getEnemies() {
        return enemyRegistry.getEnemies();
    }

    /**
     * get the event listener of the world
     * 
     * @return the event listener of the world
     */
    public WorldEventListener getEventListener() {
        return evListener;
    }

    /**
     * get a list of all the FigherEntity of the world (player, enemies, boss)
     * 
     * @return list of all the FigherEntity of the world (player, enemies, boss)
     */
    public List<FighterEntity> getFighterEntities() {
        List<FighterEntity> entities = new ArrayList<FighterEntity>();
        if (player != null) {
            entities.add((FighterEntity) player);
        }
        for (var enemy : getEnemies()) {
            entities.add(enemy);
        }
        if (wizardBoss != null) {
            entities.add(wizardBoss);
        }
        return entities;
    }

    private boolean isGameInPause() {
        return (getMiniMap() != null && getMiniMap().isShow()) || (getInventory() != null && getInventory().isShow()) || (getQuestJournal() != null && getQuestJournal().isShow());
    }

    /**
     * update all the physics components of all the entities of the world
     * 
     * @param dt the time passed since the last update
     */
    public void updateState(long dt) {
        if (loadingBar != null) {
            if (loadingBar.loadingIsStarted()) {
                loadingBar.advanceLoadingTime();
                if (loadingBar.isLoaded()) {
                    notifyWorldEvent(new ChangeWorldEvent(TileManager.HUB_MAP_ID, this));
                }
            }
        } else {

            if (isGameInPause()) {
                return;
            }
            processAiInput();
            if (player != null) {
                player.updatePhysics(dt, this);
                if (((FighterEntity) player).getWeapon() != null) {
                    ((FighterEntity) player).getDamagingArea().updatePhysics(dt, this);
                }
            }
            if (tileManager != null) {
                tileManager.updatePhysics(dt, this);
            }
            if (miniMap != null) {
                miniMap.updatePhysics(dt, this);
            }
            if (tp != null) {
                tp.updatePhysics(dt, this);
            }

            this.interractableAreas.forEach(area -> area.updatePhysics(dt, this));
            for (var enemy : getEnemies()) {
                enemy.updatePhysics(dt, this);
                if (enemy.getWeapon() != null) {
                    enemy.getDamagingArea().updatePhysics(dt, this);
                }
            }

            if (wizardBoss != null) {
                wizardBoss.updatePhysics(dt, this);
                if (wizardBoss.getWeapon() != null) {
                    wizardBoss.getDamagingArea().updatePhysics(dt, this);
                }
            }
        }
    }

    /**
     * process the input of all the enemies of the world
     * 
     */
    public void processAiInput() {
        for (var enemy : getEnemies()) {
            enemy.updateInput(null, this);
        }
        generateEnemy();
    }

    /**
     * notify the event listener of the world of a new event, add it to the event
     * queue in the gameEngine
     * 
     * @param ev the new event
     */
    public void notifyWorldEvent(WorldEvent ev) {
        evListener.notifyEvent(ev);
    }

    /**
     * get the main bounding box of the world
     * 
     * @return the main bounding box of the world
     */
    public RectBoundingBox getBBox() {
        return mainBBox;
    }

    /**
     * get the player of the world
     * 
     * @return the player of the world
     */
    public GameObject getPlayer() {
        return player;
    }

    /**
     * get the tile manager of the world
     * 
     * @return the tile manager of the world
     */
    public TileManager getTileManager() {
        return tileManager;
    }

    /**
     * get the health bar of the world
     * 
     * @return the minimap of the world
     */
    public HidableObject getMiniMap() {
        return miniMap;
    }

    /**
     * get the quest journal of the world
     * 
     * @return the quest journal of the world
     */
    public HidableObject getQuestJournal() {
        return questJournal;
    }

    /**
     * set the quest journal of the world
     * 
     * @param questJournal the new quest journal
     */
    public void setQuestJournal(HidableObject questJournal) {
        this.questJournal = questJournal;
    }

    /**
     * get the navigator line of the world
     * 
     * @return the navigator line of the world
     */
    public NavigatorLine getNavigatorLine() {
        return navigatorLine;
    }

    /**
     * get the teleporter of the world
     * 
     * @return the teleporter of the world
     */
    public Teleporter getTeleporter() {
        return tp;
    }

    /**
     * get all the scene entities of the world (FigherEntity, TileManager,
     * Teleporter, HealthBar, MiniMap, QuestJournal, Inventory, InterractableArea)
     * 
     * @return list of all the entities of the world
     */
    public List<GameObject> getSceneEntities() {
        List<GameObject> entities = new ArrayList<GameObject>();
        if (tileManager != null)
            entities.add(tileManager);
        if (player != null)
            entities.add(player);
        if (wizardBoss != null)
            entities.add(wizardBoss);
        if (navigatorLine != null)
            entities.add(navigatorLine);
        if (tp != null)
            entities.add(tp);
        for (var enemy : getEnemies()) {
            entities.add(enemy);
        }
        if (healthBar != null)
            entities.add(healthBar);
        if (miniMap != null)
            entities.add(miniMap);
        if (questJournal != null)
            entities.add(questJournal);

        if (inventory != null)
            entities.add(inventory);

        this.interractableAreas.forEach(area -> entities.add(area));
        return entities;
    }

    /**
     * load a new map in the world
     * 
     * @param map the id of the new map to load
     */
    public void laodMap(int map) {
        if (tileManager == null)
            return;
        var box = tileManager.loadMap(map);
        mainBBox = box;
    }

    /**
     * check if the player is colliding with the boundaries of the world
     * 
     * @param pos the position of the player
     * @param box the bounding box of the player
     * @return an optional containing the collision if there is one, empty otherwise
     */
    public Optional<BoundaryCollision> checkCollisionWithBoundaries(Point2d pos, RectBoundingBox box) {
        Point2d ul = mainBBox.getULCorner();
        Point2d br = mainBBox.getBRCorner();
        RectBoundingBox rect = new RectBoundingBox(pos, box.getWidth(), box.getHeight());
        if (rect.getULCorner().y < ul.y) {
            return Optional.of(new BoundaryCollision(BoundaryCollision.CollisionEdge.TOP, new Point2d(pos.x, ul.y)));
        } else if (rect.getBRCorner().y > br.y) {
            return Optional.of(new BoundaryCollision(BoundaryCollision.CollisionEdge.BOTTOM,
                    new Point2d(pos.x, br.y - rect.getHeight())));
        } else if (rect.getBRCorner().x > br.x) {
            return Optional.of(new BoundaryCollision(BoundaryCollision.CollisionEdge.RIGHT,
                    new Point2d(br.x - rect.getWidth(), pos.y)));
        } else if (rect.getULCorner().x < ul.x) {
            return Optional.of(new BoundaryCollision(BoundaryCollision.CollisionEdge.LEFT, new Point2d(ul.x, pos.y)));
        } else {
            return Optional.empty();
        }
    }

    /**
     * get the EnemyRegistry of the world
     * 
     * @return the EnemyRegistry of the world
     */
    public EnemyRegistry getEnemyRegistry() {
        return enemyRegistry;
    }

    /**
     * add an enemy to the world
     * 
     * @param pos  the position of the new enemy
     * @param conf the configuration of the new enemy
     */
    public void addEnemy(Point2d pos, EnemyConfiguration conf) {
        enemyRegistry.addEnemy(pos, conf);
    }

    /**
     * remove an enemy from the world
     * 
     * @param enemyIdentifier the identifier of the enemy to remove
     * @param killed          true if the enemy was killed, false otherwise
     */
    public void removeEnemy(int enemyIdentifier, boolean killed) {
        if (killed) {
            notifyWorldEvent(new KilledEnemyEvent(enemyRegistry.getEnemy(enemyIdentifier).getEnemyType()));
        }
        enemyRegistry.removeEnemy(enemyIdentifier);
    }

    /**
     * generate a new enemy in the world
     */
    public void generateEnemy() {
        enemyRegistry.generateEnemy(this);
    }

    /**
     * disable all the enemies of the world
     */
    public void disableEnemies() {
        enemyRegistry.disableEnemies();
    }

    /**
     * enable all the enemies of the world
     */
    public void enableEnemies() {
        enemyRegistry.enableEnemies();
    }

    /**
     * set the spawn of the enemies of the world to active or not
     * 
     * @param active true if the spawn is active, false otherwise
     */
    public void setEnemySpawnActive(boolean active) {
        if (active) {
            enemyRegistry.resumeSpawn();
        } else {
            enemyRegistry.pauseSpawn();
        }
    }

    /**
     * notify the GameEngine that the boss was killed
     */
    public void handleBossKilled() {
        notifyWorldEvent(new GameOverEvent(WinnerType.PLAYER));
        setWizardBoss(GameFactory.getInstance().createWizardBoss(this, this.getWizardBoss().getLevel() + 1));
    }

    /**
     * notify the GameEngine that the player was killed
     */
    public void handlePlayerKilled() {
        notifyWorldEvent(new GameOverEvent(WinnerType.ENEMY));
    }
}
