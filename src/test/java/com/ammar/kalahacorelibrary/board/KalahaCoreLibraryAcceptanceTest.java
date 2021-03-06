package com.ammar.kalahacorelibrary.board;

import com.ammar.kalahacorelibrary.player.PlayerType;
import com.ammar.kalahacorelibrary.pubsub.pit.impl.KalahaPit;
import com.ammar.kalahacorelibrary.pubsub.pit.impl.NormalPit;
import com.ammar.kalahacorelibrary.pubsub.referee.Referee;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Kalaha Core Library Acceptance Tests
 * <p>
 * <pre>
 *
 * The board in this test looks like the following:
 *
 *     -------------------------------------------------------------------------
 *     |             Pit12   Pit11   Pit10   Pit9    Pit8    Pit7              |
 *     |                                                                       |
 *     | KalahaPit2                                                 KalahaPit1 |
 *     |                                                                       |
 *     |             Pit1    Pit2    Pit3    Pit4    Pit5    Pit6              |
 *     -------------------------------------------------------------------------
 *
 *     Player 1 owns:  Pit 1 .. 6   +  KalahaPit1
 *     Player 2 owns:  Pit 7 .. 12  +  KalahaPit2
 *
 * </pre>
 * <p>
 * Created by amhamid on 7/23/15.
 */
public class KalahaCoreLibraryAcceptanceTest {

    /**
     * Scenario 1:
     * Player 1: Moving 6 seeds from Pit 1
     * <p>
     * Expectations:
     * - Pit 1 has 0 seeds after initial move
     * - Pit 2, 3, 4, 5, 6, should have 7 seeds
     * - Kalaha Pit Player 1 should have 1 seed
     * - Referee should note that Pit 1 is empty
     * - Referee should note that the rest of the pits (2..12) excluding Kalaha pit, are not empty
     * - Player 1 should be playing again (because last seed ends in his Kalaha pit)
     * <p>
     */
    @Test
    public void scenario1() {
        System.out.printf("\nStart test scenario 1\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        final NormalPit pit1 = kalahaBoard.getPlayer1().getPit1();
        pit1.initialMove();

        // assert number of seeds
        assertThat("Pit 1 should have 0 seeds", pit1.getNumberOfSeeds(), is(0));
        assertThat("Pit 2 should have 7 seeds", kalahaBoard.getPlayer1().getPit2().getNumberOfSeeds(), is(7));
        assertThat("Pit 3 should have 7 seeds", kalahaBoard.getPlayer1().getPit3().getNumberOfSeeds(), is(7));
        assertThat("Pit 4 should have 7 seeds", kalahaBoard.getPlayer1().getPit4().getNumberOfSeeds(), is(7));
        assertThat("Pit 5 should have 7 seeds", kalahaBoard.getPlayer1().getPit5().getNumberOfSeeds(), is(7));
        assertThat("Pit 6 should have 7 seeds", kalahaBoard.getPlayer1().getPit6().getNumberOfSeeds(), is(7));
        assertThat("Kalaha Pit Player 1 should have 1 seed", kalahaBoard.getPlayer1().getKalahaPit().getNumberOfSeeds(), is(1));

        final Referee referee = kalahaBoard.getReferee();
        final Map<PlayerType, Set<String>> emptyPits = referee.getEmptyPits();
        final Map<PlayerType, Set<String>> notEmptyPits = referee.getNotEmptyPits();

        // assert empty pit for player 1
        final Set<String> emptyPitIdentifiers = emptyPits.get(PlayerType.PLAYER_1);
        assertThat("There should be one empty pit for player 1", emptyPitIdentifiers.size(), is(1));
        assertThat("Pit 1 should be empty pit for player 1", emptyPitIdentifiers.contains("Pit 1"), is(true));

        // assert not empty pit for player 1
        final Set<String> notEmptyPitIdentifiers = notEmptyPits.get(PlayerType.PLAYER_1);
        assertThat("There should be five not empty pits for player 1 (excluding Kalaha for player 1)", notEmptyPitIdentifiers.size(), is(5));
        assertThat("Pit 2 should be not empty pit for player 1", notEmptyPitIdentifiers.contains("Pit 2"), is(true));
        assertThat("Pit 3 should be not empty pit for player 1", notEmptyPitIdentifiers.contains("Pit 3"), is(true));
        assertThat("Pit 4 should be not empty pit for player 1", notEmptyPitIdentifiers.contains("Pit 4"), is(true));
        assertThat("Pit 5 should be not empty pit for player 1", notEmptyPitIdentifiers.contains("Pit 5"), is(true));
        assertThat("Pit 6 should be not empty pit for player 1", notEmptyPitIdentifiers.contains("Pit 6"), is(true));

        // assert empty pit for player 2
        assertThat("There should be no empty pit for player 2", emptyPits.get(PlayerType.PLAYER_2).size(), is(0));

        // assert not empty pit for player 2
        final Set<String> notEmptyPitIdentifiersForPlayer2 = notEmptyPits.get(PlayerType.PLAYER_2);
        assertThat("There should be six not empty pits for player 2 (excluding Kalaha for player 2)", notEmptyPitIdentifiersForPlayer2.size(), is(6));
        assertThat("Pit 7 should be not empty pit for player 2", notEmptyPitIdentifiersForPlayer2.contains("Pit 7"), is(true));
        assertThat("Pit 8 should be not empty pit for player 2", notEmptyPitIdentifiersForPlayer2.contains("Pit 8"), is(true));
        assertThat("Pit 9 should be not empty pit for player 2", notEmptyPitIdentifiersForPlayer2.contains("Pit 9"), is(true));
        assertThat("Pit 10 should be not empty pit for player 2", notEmptyPitIdentifiersForPlayer2.contains("Pit 10"), is(true));
        assertThat("Pit 11 should be not empty pit for player 2", notEmptyPitIdentifiersForPlayer2.contains("Pit 11"), is(true));
        assertThat("Pit 12 should be not empty pit for player 2", notEmptyPitIdentifiersForPlayer2.contains("Pit 12"), is(true));

        final PlayerType currentPlayerTurn = referee.getCurrentPlayerTurn();
        assertThat("Current player should be player 1", currentPlayerTurn, is(PlayerType.PLAYER_1));
    }

    /**
     * Scenario 2:
     * Do scenario 1 from the perspective of player 2
     * <p>
     * Player 2: Moving 6 seeds from Pit 7 (player 2 pit 1)
     * <p>
     * Expectations:
     * - Pit 7 has 0 seeds after initial move
     * - Pit 8, 9, 10, 11, 12, should have 7 seeds
     * - Kalaha Pit Player 2 should have 1 seed
     * - Referee should note that Pit 7 is empty
     * - Referee should note that the rest of the pits (1..6 and 8..12) excluding Kalaha pit, are not empty
     * - Player 2 should be playing again (because last seed ends in his Kalaha pit)
     * <p>
     */
    @Test
    public void scenario2() {
        System.out.printf("\nStart test scenario 2\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        final NormalPit pit7 = kalahaBoard.getPlayer2().getPit1();
        pit7.initialMove();

        // assert number of seeds
        assertThat("Pit 7 should have 0 seeds", pit7.getNumberOfSeeds(), is(0));
        assertThat("Pit 8 should have 7 seeds", kalahaBoard.getPlayer2().getPit2().getNumberOfSeeds(), is(7));
        assertThat("Pit 9 should have 7 seeds", kalahaBoard.getPlayer2().getPit3().getNumberOfSeeds(), is(7));
        assertThat("Pit 10 should have 7 seeds", kalahaBoard.getPlayer2().getPit4().getNumberOfSeeds(), is(7));
        assertThat("Pit 11 should have 7 seeds", kalahaBoard.getPlayer2().getPit5().getNumberOfSeeds(), is(7));
        assertThat("Pit 12 should have 7 seeds", kalahaBoard.getPlayer2().getPit6().getNumberOfSeeds(), is(7));
        assertThat("Kalaha Pit Player 2 should have 1 seed", kalahaBoard.getPlayer2().getKalahaPit().getNumberOfSeeds(), is(1));

        final Referee referee = kalahaBoard.getReferee();
        final Map<PlayerType, Set<String>> emptyPits = referee.getEmptyPits();
        final Map<PlayerType, Set<String>> notEmptyPits = referee.getNotEmptyPits();

        // assert empty pit for player 2
        final Set<String> emptyPitIdentifiers = emptyPits.get(PlayerType.PLAYER_2);
        assertThat("There should be one empty pit for player 2", emptyPitIdentifiers.size(), is(1));
        assertThat("Pit 7 should be empty pit for player 2", emptyPitIdentifiers.contains("Pit 7"), is(true));

        // assert not empty pit for player 2
        final Set<String> notEmptyPitIdentifiers = notEmptyPits.get(PlayerType.PLAYER_2);
        assertThat("There should be five not empty pits for player 2 (excluding Kalaha for player 2)", notEmptyPitIdentifiers.size(), is(5));
        assertThat("Pit 8 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 8"), is(true));
        assertThat("Pit 9 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 9"), is(true));
        assertThat("Pit 10 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 10"), is(true));
        assertThat("Pit 11 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 11"), is(true));
        assertThat("Pit 12 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 12"), is(true));

        // assert empty pit for player 1
        assertThat("There should be no empty pit for player 1", emptyPits.get(PlayerType.PLAYER_1).size(), is(0));

        // assert not empty pit for player 1
        final Set<String> notEmptyPitIdentifiersForPlayer2 = notEmptyPits.get(PlayerType.PLAYER_1);
        assertThat("There should be six not empty pits for player 1 (excluding Kalaha for player 1)", notEmptyPitIdentifiersForPlayer2.size(), is(6));
        assertThat("Pit 1 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 1"), is(true));
        assertThat("Pit 2 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 2"), is(true));
        assertThat("Pit 3 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 3"), is(true));
        assertThat("Pit 4 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 4"), is(true));
        assertThat("Pit 5 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 5"), is(true));
        assertThat("Pit 6 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 6"), is(true));

        final PlayerType currentPlayerTurn = referee.getCurrentPlayerTurn();
        assertThat("Current player should be player 2", currentPlayerTurn, is(PlayerType.PLAYER_2));
    }

    /**
     * Scenario 3:
     * Player 1: Moving 14 seeds from pit 1.
     * <p>
     * Expectations:
     * - Should add 1 to all of its neighbors and should stop at pit 2 (next round)
     * - Should skip the Kalaha opponent
     * - Switch to player 2, since last seed ends not in player 1's Kalaha pit
     */
    @Test
    public void scenario3() {
        System.out.printf("\nStart test scenario 3\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        final NormalPit pit1 = kalahaBoard.getPlayer1().getPit1();
        pit1.setNumberOfSeedsForTestPurposeOnly(14);
        pit1.initialMove();

        // assert number of seeds
        // initially pit 1 has 0 seed and pit 2 has 7 seeds, but will be updated in the second round (because we have 14 seeds as initial seeds for pit 1)
        assertThat("Pit 3 should have 7 seeds", kalahaBoard.getPlayer1().getPit3().getNumberOfSeeds(), is(7));
        assertThat("Pit 4 should have 7 seeds", kalahaBoard.getPlayer1().getPit4().getNumberOfSeeds(), is(7));
        assertThat("Pit 5 should have 7 seeds", kalahaBoard.getPlayer1().getPit5().getNumberOfSeeds(), is(7));
        assertThat("Pit 6 should have 7 seeds", kalahaBoard.getPlayer1().getPit6().getNumberOfSeeds(), is(7));
        assertThat("Kalaha Pit Player 1 should have 1 seed", kalahaBoard.getPlayer1().getKalahaPit().getNumberOfSeeds(), is(1));
        assertThat("Pit 7 should have 7 seeds", kalahaBoard.getPlayer2().getPit1().getNumberOfSeeds(), is(7));
        assertThat("Pit 8 should have 7 seeds", kalahaBoard.getPlayer2().getPit2().getNumberOfSeeds(), is(7));
        assertThat("Pit 9 should have 7 seeds", kalahaBoard.getPlayer2().getPit3().getNumberOfSeeds(), is(7));
        assertThat("Pit 10 should have 7 seeds", kalahaBoard.getPlayer2().getPit4().getNumberOfSeeds(), is(7));
        assertThat("Pit 11 should have 7 seeds", kalahaBoard.getPlayer2().getPit5().getNumberOfSeeds(), is(7));
        assertThat("Pit 12 should have 7 seeds", kalahaBoard.getPlayer2().getPit6().getNumberOfSeeds(), is(7));
        assertThat("Kalaha Pit Player 2 should have 0 seed", kalahaBoard.getPlayer2().getKalahaPit().getNumberOfSeeds(), is(0));
        assertThat("Pit 1 should have 1 seeds", kalahaBoard.getPlayer1().getPit1().getNumberOfSeeds(), is(1));
        assertThat("Pit 2 should have 8 seeds", kalahaBoard.getPlayer1().getPit2().getNumberOfSeeds(), is(8));

        final Referee referee = kalahaBoard.getReferee();
        final Map<PlayerType, Set<String>> emptyPits = referee.getEmptyPits();
        final Map<PlayerType, Set<String>> notEmptyPits = referee.getNotEmptyPits();

        // assert empty pit for player 1
        final Set<String> emptyPitIdentifiers = emptyPits.get(PlayerType.PLAYER_1);
        assertThat("There should be no empty pit for player 1", emptyPitIdentifiers.size(), is(0));

        // assert not empty pit for player 1
        final Set<String> notEmptyPitIdentifiers = notEmptyPits.get(PlayerType.PLAYER_1);
        assertThat("There should be six not empty pits for player 1 (excluding Kalaha for player 1)", notEmptyPitIdentifiers.size(), is(6));
        assertThat("Pit 1 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 1"), is(true));
        assertThat("Pit 2 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 2"), is(true));
        assertThat("Pit 3 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 3"), is(true));
        assertThat("Pit 4 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 4"), is(true));
        assertThat("Pit 5 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 5"), is(true));
        assertThat("Pit 6 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 6"), is(true));

        // assert empty pit for player 2
        assertThat("There should be no empty pit for player 2", emptyPits.get(PlayerType.PLAYER_2).size(), is(0));

        // assert not empty pit for player 2
        final Set<String> notEmptyPitIdentifiersForPlayer2 = notEmptyPits.get(PlayerType.PLAYER_2);
        assertThat("There should be six not empty pits for player 2 (excluding Kalaha for player 2)", notEmptyPitIdentifiersForPlayer2.size(), is(6));
        assertThat("Pit 7 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 7"), is(true));
        assertThat("Pit 8 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 8"), is(true));
        assertThat("Pit 9 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 9"), is(true));
        assertThat("Pit 10 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 10"), is(true));
        assertThat("Pit 11 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 11"), is(true));
        assertThat("Pit 12 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 12"), is(true));

        final PlayerType currentPlayerTurn = referee.getCurrentPlayerTurn();
        assertThat("Current player should switch to player 2", currentPlayerTurn, is(PlayerType.PLAYER_2));
    }

    /**
     * Scenario 4:
     * Do scenario 3 from the perspective of player 2
     * <p>
     * Player 2: Moving 14 seeds from pit 7.
     * <p>
     * Expectations:
     * - Should add 1 to all of its neighbors and should stop at pit 8 (next round)
     * - Should skip the Kalaha opponent
     * - Switch to player 1, since last seed ends not in player 2's Kalaha pit
     */
    @Test
    public void scenario4() {
        System.out.printf("\nStart test scenario 4\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        final NormalPit pit7 = kalahaBoard.getPlayer2().getPit1();
        pit7.setNumberOfSeedsForTestPurposeOnly(14);
        pit7.initialMove();

        // assert number of seeds
        // initially pit 7 has 0 seed and pit 8 has 7 seeds, but will be updated in the second round (because we have 14 seeds as initial seeds for pit 7)
        assertThat("Pit 9 should have 7 seeds", kalahaBoard.getPlayer2().getPit3().getNumberOfSeeds(), is(7));
        assertThat("Pit 10 should have 7 seeds", kalahaBoard.getPlayer2().getPit4().getNumberOfSeeds(), is(7));
        assertThat("Pit 11 should have 7 seeds", kalahaBoard.getPlayer2().getPit5().getNumberOfSeeds(), is(7));
        assertThat("Pit 12 should have 7 seeds", kalahaBoard.getPlayer2().getPit6().getNumberOfSeeds(), is(7));
        assertThat("Kalaha Pit Player 2 should have 1 seed", kalahaBoard.getPlayer2().getKalahaPit().getNumberOfSeeds(), is(1));
        assertThat("Pit 1 should have 7 seeds", kalahaBoard.getPlayer1().getPit1().getNumberOfSeeds(), is(7));
        assertThat("Pit 2 should have 7 seeds", kalahaBoard.getPlayer1().getPit2().getNumberOfSeeds(), is(7));
        assertThat("Pit 3 should have 7 seeds", kalahaBoard.getPlayer1().getPit3().getNumberOfSeeds(), is(7));
        assertThat("Pit 4 should have 7 seeds", kalahaBoard.getPlayer1().getPit4().getNumberOfSeeds(), is(7));
        assertThat("Pit 5 should have 7 seeds", kalahaBoard.getPlayer1().getPit5().getNumberOfSeeds(), is(7));
        assertThat("Pit 6 should have 7 seeds", kalahaBoard.getPlayer1().getPit6().getNumberOfSeeds(), is(7));
        assertThat("Kalaha Pit Player 1 should have 0 seed", kalahaBoard.getPlayer1().getKalahaPit().getNumberOfSeeds(), is(0));
        assertThat("Pit 7 should have 1 seeds", kalahaBoard.getPlayer2().getPit1().getNumberOfSeeds(), is(1));
        assertThat("Pit 8 should have 8 seeds", kalahaBoard.getPlayer2().getPit2().getNumberOfSeeds(), is(8));

        final Referee referee = kalahaBoard.getReferee();
        final Map<PlayerType, Set<String>> emptyPits = referee.getEmptyPits();
        final Map<PlayerType, Set<String>> notEmptyPits = referee.getNotEmptyPits();

        // assert empty pit for player 2
        assertThat("There should be no empty pit for player 2", emptyPits.get(PlayerType.PLAYER_2).size(), is(0));

        // assert not empty pit for player 2
        final Set<String> notEmptyPitIdentifiersForPlayer2 = notEmptyPits.get(PlayerType.PLAYER_2);
        assertThat("There should be six not empty pits for player 2 (excluding Kalaha for player 2)", notEmptyPitIdentifiersForPlayer2.size(), is(6));
        assertThat("Pit 7 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 7"), is(true));
        assertThat("Pit 8 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 8"), is(true));
        assertThat("Pit 9 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 9"), is(true));
        assertThat("Pit 10 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 10"), is(true));
        assertThat("Pit 11 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 11"), is(true));
        assertThat("Pit 12 should be not empty pit for player 1", notEmptyPitIdentifiersForPlayer2.contains("Pit 12"), is(true));

        // assert empty pit for player 1
        final Set<String> emptyPitIdentifiers = emptyPits.get(PlayerType.PLAYER_1);
        assertThat("There should be no empty pit for player 1", emptyPitIdentifiers.size(), is(0));

        // assert not empty pit for player 1
        final Set<String> notEmptyPitIdentifiers = notEmptyPits.get(PlayerType.PLAYER_1);
        assertThat("There should be six not empty pits for player 1 (excluding Kalaha for player 1)", notEmptyPitIdentifiers.size(), is(6));
        assertThat("Pit 1 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 1"), is(true));
        assertThat("Pit 2 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 2"), is(true));
        assertThat("Pit 3 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 3"), is(true));
        assertThat("Pit 4 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 4"), is(true));
        assertThat("Pit 5 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 5"), is(true));
        assertThat("Pit 6 should be not empty pit for player 2", notEmptyPitIdentifiers.contains("Pit 6"), is(true));

        final PlayerType currentPlayerTurn = referee.getCurrentPlayerTurn();
        assertThat("Current player should switch to player 1", currentPlayerTurn, is(PlayerType.PLAYER_1));
    }

    /**
     * Scenario 5:
     * Player 1: Pit 3 has 1 seed, pit 4 is empty, player 1 moves his 1 seed from pit 3 to pit 4
     * <p>
     * Expectations:
     * - Player 1 capture his 1 seed in pit 4 and its opponent pit (who happen to have 5 pits) in the opposite pit
     * - Store these seeds (total of 6 seeds in his kalaha pit), in this scenario, his kalaha pit has originally 10 pits, so total after this event, should be 16 pits.
     * - Switch to player 2
     */
    @Test
    public void scenario5() {
        System.out.printf("\nStart test scenario 5\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        // set pit 3 to have 1 seed
        final NormalPit pit3 = kalahaBoard.getPlayer1().getPit3();
        pit3.setNumberOfSeedsForTestPurposeOnly(1);

        // set pit 4 empty
        final NormalPit pit4 = kalahaBoard.getPlayer1().getPit4();
        pit4.setNumberOfSeedsForTestPurposeOnly(0);

        // set pit 9 (the opposite of pit 4) to have 5 seeds
        final NormalPit pit9 = kalahaBoard.getPlayer2().getPit3();
        pit9.setNumberOfSeedsForTestPurposeOnly(5);

        final KalahaPit kalahaPit = kalahaBoard.getPlayer1().getKalahaPit();
        kalahaPit.setNumberOfSeedsForTestPurposeOnly(10);

        // move 1 seed from pit 3 to pit 4
        pit3.initialMove();

        assertThat("Pit 3 should have 0 seeds", kalahaBoard.getPlayer1().getPit3().getNumberOfSeeds(), is(0));
        assertThat("Pit 4 should have 0 seeds", kalahaBoard.getPlayer1().getPit4().getNumberOfSeeds(), is(0));
        assertThat("Pit 9 (the opposite of pit 4) should have 0 seeds", kalahaBoard.getPlayer2().getPit3().getNumberOfSeeds(), is(0));
        assertThat("Kalaha Pit for player 1 should have 16 seeds (10 seeds originally + 6 pits captured)", kalahaPit.getNumberOfSeeds(), is(16));

        final PlayerType currentPlayerTurn = kalahaBoard.getReferee().getCurrentPlayerTurn();
        assertThat("Current player should switch to player 2", currentPlayerTurn, is(PlayerType.PLAYER_2));
    }

    /**
     * Scenario 5:
     * Do scenario 4 from the perspective of player 2
     * <p>
     * Player 2: Pit 11 has 1 seed, pit 12 is empty, player 2 moves his 1 seed from pit 11 to pit 12
     * <p>
     * Expectations:
     * - Player 2 capture his 1 seed in pit 12 and its opponent pit (who happen to have 8 pits) in the opposite pit
     * - Store these seeds (total of 9 seeds in his kalaha pit), in this scenario, his kalaha pit has originally 10 pits, so total after this event, should be 19 pits.
     * - Switch to player 1
     */
    @Test
    public void scenario6() {
        System.out.printf("\nStart test scenario 6\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        // set pit 11 to have 1 seed
        final NormalPit pit11 = kalahaBoard.getPlayer2().getPit5();
        pit11.setNumberOfSeedsForTestPurposeOnly(1);

        // set pit 12 empty
        final NormalPit pit12 = kalahaBoard.getPlayer2().getPit6();
        pit12.setNumberOfSeedsForTestPurposeOnly(0);

        // set pit 1 (the opposite of pit 12) to have 8 seeds
        final NormalPit pit1 = kalahaBoard.getPlayer1().getPit1();
        pit1.setNumberOfSeedsForTestPurposeOnly(8);

        final KalahaPit kalahaPit = kalahaBoard.getPlayer2().getKalahaPit();
        kalahaPit.setNumberOfSeedsForTestPurposeOnly(10);

        // move 1 seed from pit 11 to pit 12
        pit11.initialMove();

        assertThat("Pit 11 should have 0 seeds", kalahaBoard.getPlayer2().getPit5().getNumberOfSeeds(), is(0));
        assertThat("Pit 12 should have 0 seeds", kalahaBoard.getPlayer2().getPit6().getNumberOfSeeds(), is(0));
        assertThat("Pit 1 (the opposite of pit 12) should have 0 seeds", kalahaBoard.getPlayer1().getPit1().getNumberOfSeeds(), is(0));
        assertThat("Kalaha Pit for player 2 should have 19 seeds (10 seeds originally + 9 pits captured)", kalahaPit.getNumberOfSeeds(), is(19));

        final PlayerType currentPlayerTurn = kalahaBoard.getReferee().getCurrentPlayerTurn();
        assertThat("Current player should switch to player 1", currentPlayerTurn, is(PlayerType.PLAYER_1));
    }

    /**
     * Scenario 7:
     * Player 1: Pit 3 has 1 seed, pit 4 is empty, player 1 moves his 1 seed from pit 3 to pit 4
     * <p>
     * Expectations:
     * - Player 1 capture his 1 seed in pit 4 and its opponent pit (who happen to have 0 pits) in the opposite pit
     * - Store these seeds (total of 1 seeds in his kalaha pit), in this scenario, his kalaha pit has originally 10 pits, so total after this event, should be 11 pits.
     * - Switch to player 2
     */
    @Test
    public void scenario7() {
        System.out.printf("\nStart test scenario 7\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        // set pit 3 to have 1 seed
        final NormalPit pit3 = kalahaBoard.getPlayer1().getPit3();
        pit3.setNumberOfSeedsForTestPurposeOnly(1);

        // set pit 4 empty
        final NormalPit pit4 = kalahaBoard.getPlayer1().getPit4();
        pit4.setNumberOfSeedsForTestPurposeOnly(0);

        // set pit 9 (the opposite of pit 4) to have 5 seeds
        final NormalPit pit9 = kalahaBoard.getPlayer2().getPit3();
        pit9.setNumberOfSeedsForTestPurposeOnly(0);

        final KalahaPit kalahaPit = kalahaBoard.getPlayer1().getKalahaPit();
        kalahaPit.setNumberOfSeedsForTestPurposeOnly(10);

        // move 1 seed from pit 3 to pit 4
        pit3.initialMove();

        assertThat("Pit 3 should have 0 seeds", kalahaBoard.getPlayer1().getPit3().getNumberOfSeeds(), is(0));
        assertThat("Pit 4 should have 0 seeds", kalahaBoard.getPlayer1().getPit4().getNumberOfSeeds(), is(0));
        assertThat("Pit 9 (the opposite of pit 4) should have 0 seeds", kalahaBoard.getPlayer2().getPit3().getNumberOfSeeds(), is(0));
        assertThat("Kalaha Pit for player 1 should have 11 seeds (10 seeds originally + 1 pits captured)", kalahaPit.getNumberOfSeeds(), is(11));

        final PlayerType currentPlayerTurn = kalahaBoard.getReferee().getCurrentPlayerTurn();
        assertThat("Current player should switch to player 2", currentPlayerTurn, is(PlayerType.PLAYER_2));
    }

    /**
     * Scenario 8:
     * Do scenario 7 from the perspective of player 2
     * <p>
     * Player 2: Pit 11 has 1 seed, pit 12 is empty, player 2 moves his 1 seed from pit 11 to pit 12
     * <p>
     * Expectations:
     * - Player 2 capture his 1 seed in pit 12 and its opponent pit (who happen to have 0 pits) in the opposite pit
     * - Store these seeds (total of 1 seeds in his kalaha pit), in this scenario, his kalaha pit has originally 10 pits, so total after this event, should be 11 pits.
     * - Switch to player 1
     */
    @Test
    public void scenario8() {
        System.out.printf("\nStart test scenario 8\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        // set pit 11 to have 1 seed
        final NormalPit pit11 = kalahaBoard.getPlayer2().getPit5();
        pit11.setNumberOfSeedsForTestPurposeOnly(1);

        // set pit 12 empty
        final NormalPit pit12 = kalahaBoard.getPlayer2().getPit6();
        pit12.setNumberOfSeedsForTestPurposeOnly(0);

        // set pit 1 (the opposite of pit 12) to have 8 seeds
        final NormalPit pit1 = kalahaBoard.getPlayer1().getPit1();
        pit1.setNumberOfSeedsForTestPurposeOnly(0);

        final KalahaPit kalahaPit = kalahaBoard.getPlayer2().getKalahaPit();
        kalahaPit.setNumberOfSeedsForTestPurposeOnly(10);

        // move 1 seed from pit 11 to pit 12
        pit11.initialMove();

        assertThat("Pit 11 should have 0 seeds", kalahaBoard.getPlayer2().getPit5().getNumberOfSeeds(), is(0));
        assertThat("Pit 12 should have 0 seeds", kalahaBoard.getPlayer2().getPit6().getNumberOfSeeds(), is(0));
        assertThat("Pit 1 (the opposite of pit 12) should have 0 seeds", kalahaBoard.getPlayer1().getPit1().getNumberOfSeeds(), is(0));
        assertThat("Kalaha Pit for player 2 should have 11 seeds (10 seeds originally + 1 pits captured)", kalahaPit.getNumberOfSeeds(), is(11));

        final PlayerType currentPlayerTurn = kalahaBoard.getReferee().getCurrentPlayerTurn();
        assertThat("Current player should switch to player 1", currentPlayerTurn, is(PlayerType.PLAYER_1));
    }


    /**
     * Scenario 9:
     * Player 1: all of his pits is empty, except pit 6 that has 1 seed, it is his turn then he move his last seed in pit 6 to his kalaha pit,
     * <p>
     * Expectations:
     * - Since all pits is empty and Kalaha pit for player 1 has more than player 2 has (50 and 36 respectively), therefore player 1 wins.
     */
    @Test
    public void scenario9() {
        System.out.printf("\nStart test scenario 9\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        // make pit 1..5 empty
        kalahaBoard.getPlayer1().getPit1().removeAllSeedForTestPurposeOnly();
        kalahaBoard.getPlayer1().getPit2().removeAllSeedForTestPurposeOnly();
        kalahaBoard.getPlayer1().getPit3().removeAllSeedForTestPurposeOnly();
        kalahaBoard.getPlayer1().getPit4().removeAllSeedForTestPurposeOnly();
        kalahaBoard.getPlayer1().getPit5().removeAllSeedForTestPurposeOnly();
        kalahaBoard.getPlayer1().getKalahaPit().setNumberOfSeedsForTestPurposeOnly(50);

        final NormalPit pit6 = kalahaBoard.getPlayer1().getPit6();
        pit6.setNumberOfSeedsForTestPurposeOnly(1);

        pit6.initialMove();

        assertThat("Player 1 wins", kalahaBoard.getReferee().getWinner(), is(PlayerType.PLAYER_1));
    }

    /**
     * Scenario 10:
     * Do scenario 9 from player 2 perspective
     * <p>
     * Player 2: all of his pits is empty, except pit 12 that has 1 seed, it is his turn then he move his last seed in pit 12 to his kalaha pit,
     * <p>
     * Expectations:
     * - Since all pits is empty and Kalaha pit for player 2 has more than player 1 has (50 and 36 respectively), therefore player 2 wins.
     */
    @Test
    public void scenario10() {
        System.out.printf("\nStart test scenario 10\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        // make pit 7..12 empty
        kalahaBoard.getPlayer2().getPit1().removeAllSeedForTestPurposeOnly();
        kalahaBoard.getPlayer2().getPit2().removeAllSeedForTestPurposeOnly();
        kalahaBoard.getPlayer2().getPit3().removeAllSeedForTestPurposeOnly();
        kalahaBoard.getPlayer2().getPit4().removeAllSeedForTestPurposeOnly();
        kalahaBoard.getPlayer2().getPit5().removeAllSeedForTestPurposeOnly();
        kalahaBoard.getPlayer2().getKalahaPit().setNumberOfSeedsForTestPurposeOnly(50);

        final NormalPit pit12 = kalahaBoard.getPlayer2().getPit6();
        pit12.setNumberOfSeedsForTestPurposeOnly(1);

        pit12.initialMove();

        assertThat("Player 2 wins", kalahaBoard.getReferee().getWinner(), is(PlayerType.PLAYER_2));
    }

    /**
     * Scenario 11:
     * <p>
     * Simulate TIE game from player 1 perspective
     */
    @Test
    public void scenario11() {
        System.out.printf("\nStart test scenario 11\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        kalahaBoard.getPlayer1().getKalahaPit().setNumberOfSeedsForTestPurposeOnly(35);

        final NormalPit pit6 = kalahaBoard.getPlayer1().getPit6();
        pit6.setNumberOfSeedsForTestPurposeOnly(1);

        pit6.initialMove();

        assertThat("TIE game", kalahaBoard.getReferee().getWinner(), is(nullValue()));
    }

    /**
     * Scenario 12:
     * <p>
     * Simulate TIE game from player 2 perspective
     */
    @Test
    public void scenario12() {
        System.out.printf("\nStart test scenario 12\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        kalahaBoard.getPlayer2().getKalahaPit().setNumberOfSeedsForTestPurposeOnly(35);

        final NormalPit pit12 = kalahaBoard.getPlayer2().getPit6();
        pit12.setNumberOfSeedsForTestPurposeOnly(1);

        pit12.initialMove();

        assertThat("TIE game", kalahaBoard.getReferee().getWinner(), is(nullValue()));
    }

    /**
     * Scenario 13:
     * Player 1: Pit 6 has 2 seeds, pit 7 is empty, player 1 moves his 2 seeds from pit 6 to pit 7
     * <p>
     * Expectations:
     * - Player 1 doesn't capture anything
     * - Switch to player 2
     */
    @Test
    public void scenario13() {
        System.out.printf("\nStart test scenario 13\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        // set pit 6 to have 2 seed
        final NormalPit pit6 = kalahaBoard.getPlayer1().getPit6();
        pit6.setNumberOfSeedsForTestPurposeOnly(2);

        // set pit 7 empty
        final NormalPit pit7 = kalahaBoard.getPlayer2().getPit1();
        pit7.setNumberOfSeedsForTestPurposeOnly(0);

        // move 2 seed from pit 6 to pit 7
        pit6.initialMove();

        assertThat("Pit 6 should have 0 seeds", kalahaBoard.getPlayer1().getPit6().getNumberOfSeeds(), is(0));
        assertThat("Pit 7 should have 1 seeds", kalahaBoard.getPlayer2().getPit1().getNumberOfSeeds(), is(1));
        assertThat("Kalaha Pit for player 1 should have 1 seeds (0 seeds originally + 1 pits from pit 6) --> no capture happens", kalahaBoard.getPlayer1().getKalahaPit().getNumberOfSeeds(), is(1));

        final PlayerType currentPlayerTurn = kalahaBoard.getReferee().getCurrentPlayerTurn();
        assertThat("Current player should switch to player 2", currentPlayerTurn, is(PlayerType.PLAYER_2));
    }

    /**
     * Scenario 14:
     * Player 2: Pit 12 has 2 seeds, pit 1 is empty, player 2 moves his 2 seeds from pit 12 to pit 1
     * <p>
     * Expectations:
     * - Player 2 doesn't capture anything
     * - Switch to player 1
     */
    @Test
    public void scenario14() {
        System.out.printf("\nStart test scenario 14\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        // set pit 12 to have 2 seed
        final NormalPit pit12 = kalahaBoard.getPlayer2().getPit6();
        pit12.setNumberOfSeedsForTestPurposeOnly(2);

        // set pit 1 empty
        final NormalPit pit1 = kalahaBoard.getPlayer1().getPit1();
        pit1.setNumberOfSeedsForTestPurposeOnly(0);

        // move 2 seed from pit 12 to pit 1
        pit12.initialMove();

        assertThat("Pit 12 should have 0 seeds", kalahaBoard.getPlayer2().getPit6().getNumberOfSeeds(), is(0));
        assertThat("Pit 1 should have 1 seeds", kalahaBoard.getPlayer1().getPit1().getNumberOfSeeds(), is(1));
        assertThat("Kalaha Pit for player 2 should have 1 seeds (0 seeds originally + 1 pits from pit 12) --> no capture happens", kalahaBoard.getPlayer2().getKalahaPit().getNumberOfSeeds(), is(1));

        final PlayerType currentPlayerTurn = kalahaBoard.getReferee().getCurrentPlayerTurn();
        assertThat("Current player should switch to player 1", currentPlayerTurn, is(PlayerType.PLAYER_1));
    }

    /**
     * When Player 1 moves 1 seed from pit 1 and end-up in pit 2 then CHANGE_TURN should be published
     */
    @Test
    public void scenario15() {
        System.out.printf("\nStart test scenario 15\n");

        final KalahaBoard kalahaBoard = new KalahaBoard(6);

        // set pit 1 to have 1 seed
        final NormalPit pit1 = kalahaBoard.getPlayer1().getPit1();
        pit1.setNumberOfSeedsForTestPurposeOnly(1);

        // move 1 seed from pit 1 to pit 2
        pit1.initialMove();

        assertThat("Pit 1 should have 0 seeds", kalahaBoard.getPlayer1().getPit1().getNumberOfSeeds(), is(0));

        final PlayerType currentPlayerTurn = kalahaBoard.getReferee().getCurrentPlayerTurn();
        assertThat("Current player should switch to player 2", currentPlayerTurn, is(PlayerType.PLAYER_2));
    }

}
