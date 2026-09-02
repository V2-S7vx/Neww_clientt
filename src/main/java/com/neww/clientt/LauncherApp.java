package com.neww.clientt;

import javafx.animation.AnimationTimer;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LauncherApp extends Application {
    static final double W=1180, H=720;
    final Random random=new Random(42); final List<Star> stars=new ArrayList<>();
    double mx=W/2, my=H/2;

    @Override public void start(Stage stage) {
        BorderPane root=new BorderPane();
        root.setStyle("-fx-background-color:#020308;");
        Pane space=new Pane(); space.setMouseTransparent(true);
        for(int i=0;i<240;i++){ Star s=new Star(); stars.add(s); space.getChildren().add(s.dot); }
        root.setCenter(space);

        VBox content=new VBox(18); content.setPadding(new Insets(28)); content.setAlignment(Pos.CENTER_LEFT);
        Label brand=new Label("NEWW CLIENTT");
        brand.setStyle("-fx-text-fill:white;-fx-font-size:20px;-fx-font-weight:900;-fx-letter-spacing:3px;");
        Label tag=new Label("MINECRAFT LAUNCHER");
        tag.setStyle("-fx-text-fill:#687086;-fx-font-size:10px;-fx-font-weight:800;-fx-letter-spacing:2px;");
        HBox header=new HBox(16,brand,tag); header.setAlignment(Pos.CENTER_LEFT);

        Label title=new Label("Ready to play?");
        title.setStyle("-fx-text-fill:white;-fx-font-size:52px;-fx-font-weight:900;");
        Label sub=new Label("Your Minecraft. Your way. Fast, clean, and built for the stars.");
        sub.setStyle("-fx-text-fill:#858da2;-fx-font-size:15px;");

        ComboBox<String> versions=new ComboBox<>();
        versions.getItems().addAll("Latest Release","1.21.8","1.21.7","1.21.6","1.20.6");
        versions.setValue("Latest Release"); versions.setPrefWidth(240); versions.setPrefHeight(52);
        versions.setStyle("-fx-background-color:#0c1019;-fx-border-color:#252b3a;-fx-border-radius:12;-fx-background-radius:12;-fx-text-fill:white;");
        Button play=new Button("PLAY   →"); play.setPrefSize(220,52);
        play.setStyle("-fx-background-color:white;-fx-text-fill:#05060a;-fx-font-size:14px;-fx-font-weight:900;-fx-background-radius:12;-fx-cursor:hand;");
        Label status=new Label("Select a version and press Play"); status.setStyle("-fx-text-fill:#596174;-fx-font-size:12px;");
        play.setOnAction(e->status.setText("Selected " + versions.getValue() + " • Minecraft launch integration ready for the next step."));
        HBox controls=new HBox(12,versions,play);
        content.getChildren().addAll(header,title,sub,controls,status);

        Label fps=new Label("120 FPS UI"); fps.setStyle("-fx-text-fill:#a7aec0;-fx-font-size:11px;-fx-font-weight:800;-fx-background-color:#0b0e16;-fx-background-radius:20;-fx-padding:8 13;");
        Label footer=new Label("JAVA 21  •  STARFIELD ENABLED  •  NEW CLIENTT"); footer.setStyle("-fx-text-fill:#464e61;-fx-font-size:10px;-fx-font-weight:800;-fx-letter-spacing:1.5px;");
        BorderPane overlay=new BorderPane(); overlay.setPadding(new Insets(24,28,24,28)); overlay.setTop(new HBox(20,content,fps)); overlay.setBottom(footer);
        root.setTop(overlay); BorderPane.setAlignment(overlay,Pos.CENTER_LEFT);

        Scene scene=new Scene(root,W,H,Color.BLACK);
        scene.setOnMouseMoved(e->{mx=e.getX();my=e.getY();}); stage.setScene(scene); stage.setTitle("Neww Clientt — Minecraft Launcher"); stage.show();

        new AnimationTimer(){ long last=System.nanoTime(); public void handle(long now){ double dt=Math.min((now-last)/1e9,0.05); last=now; for(Star s:stars)s.update(dt,mx,my); }}.start();
    }

    final class Star {
        final Circle dot=new Circle(); double x,y,vx,vy,size,phase;
        Star(){ reset(true); }
        void reset(boolean initial){ x=random.nextDouble()*W; y=initial?random.nextDouble()*H:H+8; vy=10+random.nextDouble()*34; vx=-3+random.nextDouble()*6; size=.5+random.nextDouble()*1.8; phase=random.nextDouble()*Math.PI*2; dot.setRadius(size); dot.setFill(Color.rgb(225,232,255,.2+random.nextDouble()*.7)); }
        void update(double dt,double mouseX,double mouseY){ y+=vy*dt; x+=(vx+Math.sin(y*.012+phase)*.7)*dt; if(y>H+10)reset(false); if(x<-10)x=W+10; if(x>W+10)x=-10; dot.setTranslateX(x+(mouseX-W/2)*.003*size); dot.setTranslateY(y+(mouseY-H/2)*.002*size); }
    }
    public static void main(String[] args){launch(args);}
}
