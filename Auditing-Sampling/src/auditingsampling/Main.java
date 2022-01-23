package auditingsampling;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class Main extends Application {

	private Label lbResult=new Label();
	private ComboBox<String> cmSamplingType; 
	private Label lbConfidenceLevel;
	private ComboBox<Double> cbConfidenceLevel;
	private ObservableList<Double> z = FXCollections.observableArrayList();
	private Label lbSD;
	private TextField txSD;
	private Label lbErrorMargin;
	private ComboBox<Double> cbErrorMargin;
	private Label lbPopulationProp;
	private ComboBox<Double> cbPopulationProp;
	private Label lbErrorMarginUnits;
	private TextField txErrorMarginUnits;
	private Label lbPopulation;
	private TextField txPopulation;
	private Button btCalculate;
	private Double result;

	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Simple Sampling tool by L. v1.0");
			primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("icons/Icon.png")));
			primaryStage.setResizable(false);
			GridPane grid = new GridPane();
			Scene scene = new Scene(grid,350,450);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			lbResult.setTextFill(Color.BLUE);
			lbResult.setStyle("-fx-font-size: 14");
			lbResult.setStyle("-fx-font-weight: bold");

			VBox vbSamplingType = new VBox();
			vbSamplingType.setSpacing(5);
			vbSamplingType.setPadding(new Insets(10,50,10,50));
			vbSamplingType.setAlignment(Pos.TOP_CENTER);

			Label lbSamplingType= new Label("Sampling Type");
			ObservableList<String> samplingTypes = FXCollections.observableArrayList();
			samplingTypes.addAll("Cochran (big or unknow population) - Sample Size for One Sample, Dichotomous Outcome",
					"Cochran (small population)",
					"Slovin/Yamane", 
					"Sample Size for One Sample, Continuous Outcome");
			cmSamplingType = new ComboBox<>(samplingTypes);
			cmSamplingType.setCellFactory(param -> {
				return new ListCell<>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item);
							Tooltip tt = new MyTooltip().create_tooltip("");
							tt.setPrefWidth(400);
							switch (item){
							case "Cochran (big or unknow population) - Sample Size for One Sample, Dichotomous Outcome":
								tt.setText("The Cochran formula allows you to calculate an ideal sample size. "
										+ "Cochran’s formula is considered especially appropriate in situations with large populations. ");
								break;
							case "Cochran (small population)":
								tt.setText("Cocharan approach best suited for small population");
								break;
							case "Slovin/Yamane":
								tt.setText("Approach similar to Cochran (and others) with 95% of confidence level and population proportion of 0.5");
								break;
							case "Sample Size for One Sample, Continuous Outcome":
								tt.setText("In studies where the plan is to estimate the mean of a continuous outcome variable in a single population");
								break;
							default:
								break;
							}
							setTooltip(tt);
						} else {
							setText(null);
							setTooltip(null);
						}
					}
				};
			});
			cmSamplingType.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e){
					lbConfidenceLevel.setDisable(false);
					cbConfidenceLevel.setDisable(false);
					cbConfidenceLevel.setValue(95.0);
					lbSD.setDisable(false);
					txSD.setDisable(false);
					txSD.setText("0.0");
					lbErrorMargin.setDisable(false);
					cbErrorMargin.setDisable(false);
					cbErrorMargin.setValue(0.05);
					lbPopulationProp.setDisable(false);
					cbPopulationProp.setDisable(false);
					cbPopulationProp.setValue(0.5);
					lbErrorMarginUnits.setDisable(false);
					txErrorMarginUnits.setDisable(false);
					lbPopulation.setDisable(false);
					txPopulation.setDisable(false);
					txPopulation.setText("0");
					btCalculate.setDisable(false);
					lbResult.setText("");
					result=-1.0;
					switch (cmSamplingType.getValue()){
					case "Cochran (big or unknow population) - Sample Size for One Sample, Dichotomous Outcome":
						lbErrorMarginUnits.setDisable(true);
						txErrorMarginUnits.setDisable(true);
						lbSD.setDisable(true);
						txSD.setDisable(true);
						lbPopulation.setDisable(true);
						txPopulation.setDisable(true);
						break;
					case "Cochran (small population)":
						lbSD.setDisable(true);
						txSD.setDisable(true);
						lbErrorMarginUnits.setDisable(true);
						txErrorMarginUnits.setDisable(true);
						break;
					case "Slovin/Yamane":
						lbConfidenceLevel.setDisable(true);
						cbConfidenceLevel.setDisable(true);
						lbSD.setDisable(true);
						txSD.setDisable(true);
						lbErrorMarginUnits.setDisable(true);
						txErrorMarginUnits.setDisable(true);
						break;
					case "Sample Size for One Sample, Continuous Outcome":
						lbErrorMargin.setDisable(true);
						cbErrorMargin.setDisable(true);
						txPopulation.setDisable(true);
						lbPopulationProp.setDisable(true);
						cbPopulationProp.setDisable(true);
						break;
					default:
						break;
					}					
				}
			});

			vbSamplingType.getChildren().addAll(lbSamplingType, cmSamplingType);

			lbConfidenceLevel= new Label("Confidence Level");
			lbConfidenceLevel.setDisable(true);
			ObservableList<Double> confidenceLevel = FXCollections.observableArrayList();
			confidenceLevel.addAll(80.0, 85.0, 90.0, 95.0, 98.0, 99.0, 99.5, 99.9);
			z.addAll(1.285, 1.440, 1.645, 1.960, 2.326, 2.576, 2.807, 3.291);
			cbConfidenceLevel = new ComboBox<Double>(confidenceLevel);
			cbConfidenceLevel.setDisable(true);
			cbConfidenceLevel.setPrefWidth(100);
			cbConfidenceLevel.setTooltip(new MyTooltip().create_tooltip("Long-run frequency of confidence intervals that contain the true value of the parameter"));

			vbSamplingType.getChildren().addAll(lbConfidenceLevel, cbConfidenceLevel);

			lbSD= new Label("SD");
			lbSD.setDisable(true);
			txSD = new TextField();
			txSD.setDisable(true);
			txSD.setMaxWidth(100);
			txSD.setTooltip(new MyTooltip().create_tooltip("Amount of variation or dispersion of a set of values"));
			
			vbSamplingType.getChildren().addAll(lbSD, txSD);

			lbErrorMargin= new Label("Error Margin (%)");
			lbErrorMargin.setDisable(true);
			ObservableList<Double> em = FXCollections.observableArrayList();
			for(double i=0.01;i<=0.2;i+=0.02) em.add(Math.round(i * 100.0) / 100.0);
			cbErrorMargin = new ComboBox<Double>(em);
			cbErrorMargin.setDisable(true);
			cbErrorMargin.setTooltip(new MyTooltip().create_tooltip("Range of values below and above the sample statistic in a confidence interval."));
			cbErrorMargin.setPrefWidth(100);

			vbSamplingType.getChildren().addAll(lbErrorMargin, cbErrorMargin);

			lbErrorMarginUnits= new Label("Error Margin (units)");
			lbErrorMarginUnits.setDisable(true);
			txErrorMarginUnits = new TextField();
			txErrorMarginUnits.setDisable(true);
			txErrorMarginUnits.setMaxWidth(100);
			txErrorMarginUnits.setTooltip(new MyTooltip().create_tooltip("Range of values below and above the sample statistic in a confidence interval expressed in units."));

			vbSamplingType.getChildren().addAll(lbErrorMarginUnits, txErrorMarginUnits);

			lbPopulationProp= new Label("Population Proportion");
			lbPopulationProp.setDisable(true);
			ObservableList<Double> pp = FXCollections.observableArrayList();
			for(double i=0.05;i<=1.0;i+=0.05) pp.add(Math.round(i * 100.0) / 100.0);
			cbPopulationProp = new ComboBox<Double>(pp);
			cbPopulationProp.setDisable(true);
			cbPopulationProp.setTooltip(new MyTooltip().create_tooltip("(Estimated) proportion of the population which has the attribute in question. If unknow, select 0.5 (max.)"));
			cbPopulationProp.setPrefWidth(100);

			vbSamplingType.getChildren().addAll(lbPopulationProp, cbPopulationProp);

			lbPopulation= new Label("Population");
			lbPopulation.setDisable(true);
			txPopulation = new TextField();
			txPopulation.setDisable(true);
			txPopulation.setTooltip(new MyTooltip().create_tooltip("Whole set of elements to perform sampling"));
			txPopulation.setMaxWidth(100);

			vbSamplingType.getChildren().addAll(lbPopulation, txPopulation);

			btCalculate= new Button("Calculate");
			btCalculate.setDisable(true);
			btCalculate.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e){
					switch (cmSamplingType.getValue()){
					case "Cochran (big or unknow population) - Sample Size for One Sample, Dichotomous Outcome":
						result = (z.get(cbConfidenceLevel.getSelectionModel().getSelectedIndex()) * z.get(cbConfidenceLevel.getSelectionModel().getSelectedIndex())
								* cbPopulationProp.getValue() * (1 - cbPopulationProp.getValue())) / (cbErrorMargin.getValue() * cbErrorMargin.getValue());
						break;
					case "Cochran (small population)":
						double cochran= (z.get(cbConfidenceLevel.getSelectionModel().getSelectedIndex()) * z.get(cbConfidenceLevel.getSelectionModel().getSelectedIndex())
								* cbPopulationProp.getValue() * (1 - cbPopulationProp.getValue())) / (cbErrorMargin.getValue() * cbErrorMargin.getValue());
						try {
							result = cochran / (1 + ((cochran - 1 ) / Double.parseDouble(txPopulation.getText())));
						} catch (NumberFormatException e1) {
							show_error("Population format number not valid");
						}
						break;
					case "Slovin/Yamane": 
						try {
							result = Double.parseDouble(txPopulation.getText()) / (1.0 + (Double.parseDouble(txPopulation.getText()) * Math.pow(cbErrorMargin.getValue(), 2)));
						} catch (NumberFormatException e1) {
							show_error("Population format number not valid");
						}
						break;
					case "Sample Size for One Sample, Continuous Outcome":
						double sd=0.0;
						try {
							sd = Double.parseDouble(txSD.getText());
						} catch (NumberFormatException e1) {
							show_error("Standard Desviation format number not valid");
						}
						double emu=0.0;
						try {
							emu = Double.parseDouble(txErrorMarginUnits.getText());
							result = Math.pow(z.get(cbConfidenceLevel.getSelectionModel().getSelectedIndex()) * sd / emu , 2);
						} catch (NumberFormatException e1) {
							show_error("Error Margin format number not valid");
						}
						break;
					default:
						break;
					}
					if(result>=0) lbResult.setText(String.format("%.2f",result));
				}
			});

			vbSamplingType.getChildren().addAll(btCalculate);
			vbSamplingType.getChildren().addAll(lbResult);

			grid.add(vbSamplingType,0,0);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void show_error(String msg) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText(msg);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("icons/Icon.png").toString()));
		alert.showAndWait();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
