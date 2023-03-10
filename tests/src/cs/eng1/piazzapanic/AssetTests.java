package cs.eng1.piazzapanic;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void testLettuceChoppedAssetExists() {
        assertTrue("This test will only pass when the lettuce_chopped.png asset exists.", Gdx.files
                .internal("food/original/lettuce_chopped.png").exists());
    }
}