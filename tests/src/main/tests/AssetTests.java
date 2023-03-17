package main.tests;

import com.badlogic.gdx.Gdx;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

@RunWith(main.tests.GdxTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AssetTests {

    @Test
    public void testLettuceChoppedAssetExists() {
        assertTrue("This test will only pass when the lettuce_chopped.png asset exists.", Gdx.files
                .internal("food/original/lettuce_chopped.png").exists());
    }
}