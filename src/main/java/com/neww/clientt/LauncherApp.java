package com.neww.clientt;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LauncherApp extends Application {
    private static final double W = 1180;
    private static final double H = 720;
    private final Random random = new Random(42);
    private final List<Star> stars = new ArrayList<>();
    private double mouseX = W / 2;
    private double mouseY = H / 2;

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color:#05060a;");

        Pane space = new Pane();
        space.setMouseTransparent(true);
        for (int i = 0; i < 280; i++) {
            Star star = new Star();
            stars.add(star);
            space.getChildren().add(star.dot);
        }

        Rectangle glow = new Rectangle(760, 520);
        glow.setArcWidth(120);
        glow.setArcHeight(120);
        glow.setFill(Color.rgb(42, 68, 120, .10));
        glow.setMouseTransparent(true);

        BorderPane ui = new BorderPane();
        ui.setPadding(new Insets(26, 32, 24, 32));

        Label brand = new Label("SPACE CLIENT");
        brand.setStyle("-fx-text-fill:#ffffff;-fx-font-size:20px;-fx-font-weight:900;-fx-letter-spacing:3px;");
        Label tag = new Label("MINECRAFT LAUNCHER");
        tag.setStyle("-fx-text-fill:#687086;-fx-font-size:10px;-fx-font-weight:800;-fx-letter-spacing:2px;");
        HBox header = new HBox(16, brand, tag);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox hero = new VBox(14);
        hero.setAlignment(Pos.CENTER_LEFT);
        Label eyebrow = new Label("WELCOME BACK");
        eyebrow.setStyle("-fx-text-fill:#7f9cff;-fx-font-size:11px;-fx-font-weight:900;-fx-letter-spacing:3px;");
        Label title = new Label("Ready to play?");
        title.setStyle("-fx-text-fill:white;-fx-font-size:56px;-fx-font-weight:900;");
        Label sub = new Label("A clean, fast Minecraft launcher built around your game.");
        sub.setStyle("-fx-text-fill:#858da2;-fx-font-size:15px;");

        ComboBox<String> versions = new ComboBox<>();
        versions.getItems().addAll("Latest Release", "1.21.8", "1.21.7", "1.21.6", "1.20.6");
        versions.setValue("Latest Release");
        versions.setPrefWidth(245);
        versions.setPrefHeight(52);
        versions.setStyle("-fx-background-color:#0c1019;-fx-border-color:#252b3a;-fx-border-radius:12;-fx-background-radius:12;-fx-text-fill:white;");

        Button play = new Button("PLAY   →");
        play.setPrefSize(210, 52);
        play.setStyle("-fx-background-color:#ffffff;-fx-text-fill:#05060a;-fx-font-size:14px;-fx-font-weight:900;-fx-background-radius:12;-fx-cursor:hand;");
        Label status = new Label("Select a version and press Play");
        status.setStyle("-fx-text-fill:#596174;-fx-font-size:12px;");
        play.setOnAction(e -> status.setText("Selected " + versions.getValue() + " • Ready to launch."));

        HBox controls = new HBox(12, versions, play);
        hero.getChildren().addAll(eyebrow, title, sub, controls, status);

        Label badge = new Label("●  ONLINE");
        badge.setStyle("-fx-text-fill:#9fe3bd;-fx-font-size:11px;-fx-font-weight:900;-fx-background-color:#0b1713;-fx-background-radius:20;-fx-padding:8 13;");
        Label footer = new Label("JAVA 21   •   SPACE CLIENT   •   STARFIELD");
        footer.setStyle("-fx-text-fill:#464e61;-fx-font-size:10px;-fx-font-weight:800;-fx-letter-spacing:1.5px;");

        ui.setTop(header);
        ui.setCenter(hero);
        BorderPane.setAlignment(hero, Pos.CENTER_LEFT);
        ui.setBottom(new HBox(18, footer, badge));
        BorderPane.setAlignment(ui.getBottom(), Pos.CENTER_LEFT);

        root.getChildren().addAll(space, glow, ui);

        Scene scene = new Scene(root, W, H, Color.BLACK);
        scene.setOnMouseMoved(e -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });
        stage.setScene(scene);
        stage.setTitle("Space Client — Minecraft Launcher");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();

        FadeTransition fade = new FadeTransition(Duration.millis(800), ui);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        ScaleTransition glowPulse = new ScaleTransition(Duration.seconds(4.5), glow);
        glowPulse.setFromX(.96);
        glowPulse.setFromY(.96);
        glowPulse.setToX(1.04);
        glowPulse.setToY(1.04);
        glowPulse.setAutoReverse(true);
        glowPulse.setCycleCount(ScaleTransition.INDEFINITE);
        glowPulse.play();

        TranslateTransition heroFloat = new TranslateTransition(Duration.seconds(3.8), hero);
        heroFloat.setFromY(3);
        heroFloat.setToY(-3);
        heroFloat.setAutoReverse(true);
        heroFloat.setCycleCount(TranslateTransition.INDEFINITE);
        heroFloat.play();

        new AnimationTimer() {
            private long last = System.nanoTime();
            @Override public void handle(long now) {
                double dt = Math.min((now - last) / 1e9, .05);
                last = now;
                for (Star star : stars) star.update(dt, mouseX, mouseY);
            }
        }.start();
    }

    private final class Star {
        final Circle dot = new Circle();
        double x, y, vx, vy, size, phase;

        Star() {
            reset(true);
        }

        void reset(boolean initial) {
            x = random.nextDouble() * W;
            y = initial ? random.nextDouble() * H : H + 8;
            vy = 10 + random.nextDouble() * 34;
            vx = -3 + random.nextDouble() * 6;
            size = .5 + random.nextDouble() * 1.8;
            phase = random.nextDouble() * Math.PI * 2;
            dot.setRadius(size);
            dot.setFill(Color.rgb(225, 232, 255, .2 + random.nextDouble() * .7));
        }

        void update(double dt, double mx, double my) {
            y += vy * dt;
            x += (vx + Math.sin(y * .012 + phase) * .7) * dt;
            if (y > H + 10) reset(false);
            if (x < -10) x = W + 10;
            if (x > W + 10) x = -10;
            dot.setTranslateX(x + (mx - W / 2) * .003 * size);
            dot.setTranslateY(y + (my - H / 2) * .002 * size);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
