package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

/**
 * It takes a TrueTypeFont (ttf) and converts it to all the necessary BitmapFonts that are required
 * for the UI elements.
 *
 * @author Faran Lane, Alistair Foggin
 * @since 12-22
 */
public class FontManager implements Disposable {

    private final FreeTypeFontGenerator fontGenerator;
    private final BitmapFont titleFont;
    private final BitmapFont headerFont;
    private final BitmapFont labelFont;

    public static final ArrayList<Color> COLOUR_LIST = new ArrayList<>(java.util.List.of(
            Color.WHITE,
            Color.BLUE,
            Color.RED,
            Color.GREEN,
            Color.YELLOW,
            Color.CHARTREUSE,
            Color.CYAN,
            Color.GOLD,
            Color.LIME,
            Color.MAGENTA,
            Color.ORANGE,
            Color.PINK,
            Color.ROYAL
    ));

    public FontManager() {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/MontserratMedium.ttf"));

        FreeTypeFontParameter titleFontParameters = new FreeTypeFontParameter();
        titleFontParameters.size = 32;
        titleFontParameters.borderColor = Color.BLACK;
        titleFontParameters.borderWidth = 2.5f;
        titleFont = fontGenerator.generateFont(titleFontParameters);

        FreeTypeFontParameter headerFontParameters = new FreeTypeFontParameter();
        headerFontParameters.size = 24;
        headerFont = fontGenerator.generateFont(headerFontParameters);

        FreeTypeFontParameter labelFontParameters = new FreeTypeFontParameter();
        labelFontParameters.size = 12;
        labelFont = fontGenerator.generateFont(labelFontParameters);
    }

    public BitmapFont getTitleFont() {
        return titleFont;
    }

    public BitmapFont getHeaderFont() {
        return headerFont;
    }

    public BitmapFont getLabelFont() {
        return labelFont;
    }

    public BitmapFont generateFont(FreeTypeFontParameter parameter) {
        return fontGenerator.generateFont(parameter);
    }

    @Override
    public void dispose() {
        fontGenerator.dispose();
    }
}
