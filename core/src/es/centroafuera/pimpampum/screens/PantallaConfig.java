package es.centroafuera.pimpampum.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import es.centroafuera.pimpampum.ConfigurationManager;

public class PantallaConfig implements Screen {
    Stage stage;

    @Override
    public void show() {
        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        final VisTextButton soundButton = new VisTextButton("Sonido --> " + ConfigurationManager.isSoundEnabled());

        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ConfigurationManager.setSoundEnabled(!ConfigurationManager.isSoundEnabled());
                soundButton.setText("Sonido --> " + ConfigurationManager.isSoundEnabled());
                dispose();
            }
        });

        final VisTextButton lifesButton = new VisTextButton("Mostrar Vidas --> " + ConfigurationManager.isLifesEnabled());
        lifesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Ir a la pantalla de configuración
                ConfigurationManager.setLifesEnabled(!ConfigurationManager.isLifesEnabled());
                lifesButton.setText("Mostrar Vidas --> " + ConfigurationManager.isLifesEnabled());
                dispose();
            }
        });

        VisTextButton quitButton = new VisTextButton("Back");
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                VisUI.dispose();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new PantallaMenuPrincipal());
            }
        });

        VisLabel aboutLabel = new VisLabel("Demo libGDX\n(c) Alejandro Garcia 2020");

        // Añade filas a la tabla y añade los componentes
        table.row();
        table.add(soundButton).center().width(200).height(100).pad(5);
        table.row();
        table.add(lifesButton).center().width(200).height(50).pad(5);
        table.row();
        table.add(quitButton).center().width(200).height(50).pad(5);
        table.row();
        table.add(aboutLabel).left().width(200).height(20).pad(5);

        // pa que haga caso a los botones
        Gdx.input.setInputProcessor(stage);
    }

    private void changeText(InputEvent event) {
        System.out.println(event.getPointer());

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Pinta la UI en la pantalla

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
