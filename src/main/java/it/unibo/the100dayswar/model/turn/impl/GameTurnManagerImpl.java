package it.unibo.the100dayswar.model.turn.impl;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import it.unibo.the100dayswar.commons.utilities.impl.Pair;
import it.unibo.the100dayswar.model.bot.api.BotPlayer;
import it.unibo.the100dayswar.model.player.api.Player;
import it.unibo.the100dayswar.model.soldier.api.Soldier;
import it.unibo.the100dayswar.model.tower.api.Tower;
import it.unibo.the100dayswar.model.turn.api.GameTurnManager;
import it.unibo.the100dayswar.model.unit.api.Unit;
import it.unibo.the100dayswar.model.turn.api.GameDay;
import it.unibo.the100dayswar.model.fight.impl.BattleFactory;
import it.unibo.the100dayswar.model.map.api.GameMap;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Class that implement the interface GameTurnManager.
 */
public class GameTurnManagerImpl implements GameTurnManager {
    private static final long serialVersionUID = 1L;

    private static final int PERIOD = 4000;
    private static final int DELAY = 4000;
    private static final int MAX_TURN_WITH_NO_MOVE = 4;
    private int turn;
    private final List<Player> players;
    private int currentPlayerIndex;
    private int daysNoMove;
    private final GameDay gameDay;
    private transient Timer timer;
    private transient Runnable updateCallback;
    private transient GameMap gameMap;
    private transient java.util.function.Consumer<Pair<Integer, Integer>> towerShotCallback;

    /**
     * Constructor of GameTurnManagerImpl.
     * 
     * @param players List of player
     */
    public GameTurnManagerImpl(final List<Player> players) {
        this.turn = 1;
        this.players = new ArrayList<>(players);
        this.currentPlayerIndex = 0;
        this.daysNoMove = 0;
        this.gameDay = new GameDayImpl();
        gameDay.attach(players.get(0));
        gameDay.attach(players.get(1));
    }
    /**
     * get the current player.
     * 
     * @return the current player
     */
    @Override
    public Player getCurrentPlayer() {
        return this.players.get(getCurrentPlayerIndex());
    }
    /** 
     * {@inheritDoc}
     */
    @Override
    public int getDay() {
        return this.gameDay.getCurrentDay();
    }
    /**
     * get the current player.
     * 
     * @return the index of the current player in the list
     */
    @Override
    public int getCurrentPlayerIndex() {
        return this.currentPlayerIndex;
    }
    /**
     * get the current turn.
     * 
     * @return the current turn
     */
    @Override
    public int getCurrentTurn() {
        return this.turn;
    }
    /**
     * switch Turn to the other player.
     */
    @Override
    public void switchTurn() {
        final int otherPlayer = this.currentPlayerIndex;
        this.currentPlayerIndex = (this.currentPlayerIndex == 0) ? 1 : 0;
        increaseTurn();
        playerStartTurn();
        towerAttack(getCurrentPlayer(), this.players.get(otherPlayer));
        if (this.getCurrentPlayer() instanceof BotPlayer) {
            ((BotPlayer) getCurrentPlayer()).makeMove();
            switchTurn();
        }
        if (this.updateCallback != null) {
            this.updateCallback.run();
        }
    }
    /**
     * increase the Turn counter.
     */
    @Override
    public void increaseTurn() {
        this.turn++;
    }
    /**
     * called when a player start his turn.
     */
    @Override
    public void playerStartTurn() {
        this.daysNoMove = 0;
    }
    /**
     * unsed to automatically change turn when day passes.
     */
    @Override
    public void onDayPassed() {
        this.gameDay.increaseDay();
        this.daysNoMove++;
        if (daysNoMove >= MAX_TURN_WITH_NO_MOVE) {
            switchTurn();
        }
        if (this.gameDay.getCurrentDay() >= this.gameDay.getMaxDay()) {
            stopTimer();
        }
        if (this.updateCallback != null) {
            this.updateCallback.run();
        }
    }
    /**
     * start the timer for the day.
     */
    @Override
    public void startTimer() {
        final TimerTask dayTimer;
        this.timer = new Timer();
        dayTimer = new TimerTask() {
            @Override
            public void run() {
                onDayPassed();
            }
        };
        timer.scheduleAtFixedRate(dayTimer, DELAY, PERIOD);
    }
    /**
     * stop the timer for the day.
     */
    @Override
    public void stopTimer() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer.purge();
            this.timer = null;
        }
    }

    /**
     * Sets the game map used for line-of-sight checks during tower attacks.
     *
     * @param gameMap the current game map
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "GameMap is an interface; storing the reference is intentional and safe")
    public void setGameMap(final GameMap gameMap) {
        this.gameMap = gameMap;
    }

    /**
     * a method for tower for the attacker to attack the defender soldier.
     * When a {@link GameMap} has been injected via {@link #setGameMap(GameMap)},
     * line-of-sight checking via Bresenham is performed before dealing damage.
     *
     * @param attacker the attacking player
     * @param defender the defending player
     */
    private void towerAttack(final Player attacker, final Player defender) {
        for (final Tower t : attacker.getTowers()) {
            for (final Soldier s : defender.getSoldiers()) {
                if (BattleFactory.createBattle(t, s, gameMap).startFight(t, s) && this.towerShotCallback != null) {
                    this.towerShotCallback.accept(new Pair<>(t.getDamage(), s.currentHealth()));
                }
            }
        }
    }
    /**
     * writeObject method for serialization.
     * 
     * @param input the input stream
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    private void readObject(final java.io.ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final Pair<Player, Unit> source) {
        players.forEach(p -> {
            if (p.equals(source.getFirst())) {
                p.removeUnit(source.getSecond());
            }
        });
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setUpdateCallback(final Runnable callback) {
        this.updateCallback = callback;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setTowerShotCallback(final java.util.function.Consumer<Pair<Integer, Integer>> callback) {
        this.towerShotCallback = callback;
    }
}
