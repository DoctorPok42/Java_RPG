module com.example.demo {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;
  requires javafx.media;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires net.synedra.validatorfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;
  requires eu.hansolo.tilesfx;
  requires com.almasb.fxgl.all;
  requires com.fasterxml.jackson.annotation;
  requires com.google.gson;
  requires annotations;
  requires java.management;

  opens com.example.demo1 to javafx.fxml;

  exports com.example.demo1;
  exports Game;
}