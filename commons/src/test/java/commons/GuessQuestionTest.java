package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GuessQuestionTest {

    GuessQuestion g = new GuessQuestion("Guess the cost of the activity", new Activity("02-nft", "02/nft.png",
            "Creating an NFT", 142000L, "https://postergrind.com/how-much-energy-does-it-take-to-make-an-nft/"));

    @Test
    void getQuestion() {
        String res = "Guess the cost of the activity";
        assertEquals(res, g.getQuestion());
    }

    @Test
    void getActivity() {
        Activity expected =  new Activity("02-nft", "02/nft.png", "Creating an NFT",
                142000L, "https://postergrind.com/how-much-energy-does-it-take-to-make-an-nft/");
        assertEquals(expected, g.getActivity());
    }

    @Test
    void getCorrectGuess() {
        String exp = "142000";
        assertEquals(exp, g.getCorrectGuess());
    }

    @Test
    void testEquals() {
        GuessQuestion e = new GuessQuestion("Guess the cost of the activity", new Activity("02-nft", "02/nft.png",
                "Creating an NFT", 142000L, "https://postergrind.com/how-much-energy-does-it-take-to-make-an-nft/"));
        assertTrue(g.equals(e));
    }

    @Test
    void testNotEquals() {
        GuessQuestion n = new GuessQuestion("Don't guess the cost", new Activity("02-nft", "02/nft.png",
                "Creating an NFT", 142000L, "https://postergrind.com/how-much-energy-does-it-take-to-make-an-nft/"));
        assertFalse(g.equals(n));
    }

    @Test
    void testHashCode() {
        GuessQuestion e = new GuessQuestion("Guess the cost of the activity", new Activity("02-nft", "02/nft.png",
                "Creating an NFT", 142000L, "https://postergrind.com/how-much-energy-does-it-take-to-make-an-nft/"));
        assertEquals(g.hashCode(), e.hashCode());
    }

    @Test
    void testNotHashEquals() {
        GuessQuestion n = new GuessQuestion("Guess the cost of the activity", new Activity("02-nft", "02/nft.png",
                "Creating an NFT", 142000L, "different source"));
        assertNotEquals(g.hashCode(), n.hashCode());
    }

    @Test
    void testToString() {
        String result = "GuessQuestion{" +
                "question='" + "Guess the cost of the activity" + '\'' +
                "activity=" + "Activity{" +
                "id='" + "02-nft" + '\'' +
                ", title='" + "Creating an NFT" + '\'' +
                ", consumption_in_wh=" + "142000" +
                ", source='" + "https://postergrind.com/how-much-energy-does-it-take-to-make-an-nft/" + '\'' +
                ", image_path='" + "02/nft.png" + '\'' +
                '}' +
                '}';
        assertEquals(result, g.toString());
    }
}