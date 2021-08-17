package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {
	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemabout;
	
	@FXML
	public void onMenuItemSellerAction(){
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService((new SellerService()));
			controller.updateTableView();
		});	
	}
	@FXML
	public void onMenuItemDepartmentAction(){
		//Ação de inicialização do controller DepartmentListController
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService((new DepartmentService()));
			controller.updateTableView();
		});

	}
	@FXML
	public void onMenuItemAboutAction(){
		loadView("/gui/About.fxml", x -> {});
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}
	// para inicializar usando lambda precisa acrescentar a declaração do parametro  "Consumer <T> initializingAction"/ e não esquecer do <T>
	private synchronized <T> void loadView(String absoluteName, Consumer <T> initializingAction){
		try {	
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			//  casting para o vbox para pegar referencia para o vbox da janela principal /para acessar o conteudo do scrollPane
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			 
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			//para executar a função que for passada como argumento pelo lambda
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		} catch(IOException e) {
			Alerts.showAlert("IO exception", "Error loading view", e.getMessage(), AlertType.ERROR);
			System.out.println( e.getMessage());
		}
	}
	

}
