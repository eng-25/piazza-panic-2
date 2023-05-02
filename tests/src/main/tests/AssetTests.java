package main.tests;

import com.badlogic.gdx.Gdx;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

@RunWith(main.tests.GdxTestRunner.class)
public class AssetTests {
    //FOOD ASSETS-----------------------------------------------------------------------------------

    @Test
    public void testLettuceChoppedAssetExists() {
        assertTrue("This test will only pass when the lettuce_chopped.png asset exists.", Gdx.files
                .internal("food/original/lettuce_chopped.png").exists());
    }


    @Test
    public void testLettuceAssetExists() {
        assertTrue("This test will only pass when the lettuce.png asset exists.", Gdx.files
                .internal("food/glitch/vegetable/lettuce.png").exists());
    }

    @Test
    public void testTomatoAssetExists() {
        assertTrue("This test will only pass when the tomato.png asset exists.", Gdx.files
                .internal("food/glitch/fruit/tomato.png").exists());
    }

    @Test
    public void testTomatoChoppedAssetExists() {
        assertTrue("This test will only pass when the tomato_chopped.png asset exists.", Gdx.files
                .internal("food/original/tomato_chopped.png").exists());
    }

    @Test
    public void testBunAssetExists() {
        assertTrue("This test will only pass when the bun.png asset exists.", Gdx.files
                .internal("food/glitch/misc/bun.png").exists());
    }

    @Test
    public void testUncookedPattyAssetExists() {
        assertTrue("This test will only pass when the uncooked_patty.png asset exists.", Gdx.files
                .internal("food/original/uncooked_patty.png").exists());
    }

    @Test
    public void testCookedPattyAssetExists() {
        assertTrue("This test will only pass when the cooked_patty.png asset exists.", Gdx.files
                .internal("food/original/cooked_patty.png").exists());
    }

    @Test
    public void testBurgerAssetExists() {
        assertTrue("This test will only pass when the sandwich_burger_04.png asset exists.", Gdx.files
                .internal("food/glitch/misc/sandwich_burger_04.png").exists());
    }

    @Test
    public void testSaladAssetExists() {
        assertTrue("This test will only pass when the salad.png asset exists.", Gdx.files
                .internal("food/glitch/misc/salad.png").exists());
    }

    @Test
    public void testChef1AssetExists() {
        assertTrue("This test will only pass when the manBrown_hold.png asset exists", Gdx.files
                .internal("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png").exists());
    }

    @Test
    public void testChef2AssetExists() {
        assertTrue("This test will only pass when the womanGreen_hold.png asset exists", Gdx.files
                .internal("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Woman Green/womanGreen_hold.png").exists());
    }
}