import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

import javafx.stage.Stage;

public class GuiProject extends Application {

	public String readFile(ArrayList<String> fp, String path) {

		try {

			File file = new File(path);
			Scanner scan = new Scanner(file);

			while (scan.hasNextLine())
				fp.add(scan.nextLine());
			System.out.println("\nFile was SUCCESSFULLY read!\n");

			scan.close();

		} catch (FileNotFoundException e) {
			System.out.println("ERROR to read the file. TRY AGAIN!\n" + e);
		}
		return path;
	
		
		
	}

	public HashMap<String, Integer> wordsCounter(ArrayList<String> fp, String path) throws IOException {

		FileInputStream fileInp = new FileInputStream(path);
		Scanner s = new Scanner(fileInp);

		HashMap<String, Integer> map = new HashMap<String, Integer>();

		while (s.hasNext()) {
			String nextWord = s.next().toLowerCase();

			nextWord = nextWord.replaceAll("[^a-zA-Z]", "");

			if (map.containsKey(nextWord)) {
				int counter = map.get(nextWord) + 1;
				map.put(nextWord, counter);
			} else {
				map.put(nextWord, 1);
			}
		}
		s.close();

		System.out.println("\n****SORTED BY MOST FREQUENTLY USED WORD****\n");
		map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(20)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new))
				.forEach((a, integer) -> System.out
						.println(String.format("Word: %s\t\t| Frequency: %s times", a, integer)));
		return map;
	}

	public void start(Stage s) {

		ArrayList<String> words = new ArrayList<String>();
		GuiProject file = new GuiProject();

		String filePath = "//Users//hellenfernandes//Documents//GUI//poem.txt";

		s.setTitle("Poem Word Occurrence");

		Button btn = new Button("Click to see the Bar Chart");
		Button btn2 = new Button("Click to hide the Bar Chart");

		btn2.setVisible(false);
		NumberAxis xAxis = new NumberAxis();
		CategoryAxis yAxis = new CategoryAxis();

		yAxis.setLabel("Words");
		xAxis.setLabel("Frequency");

		final BarChart<String, Number> chart = new BarChart<String, Number>(yAxis, xAxis);
		chart.setVisible(false);

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				chart.setVisible(true);
				btn.setVisible(false);
				btn2.setVisible(true);

			}
		});
		btn2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				chart.setVisible(false);
				btn.setVisible(true);
				btn2.setVisible(false);

			}
		});

		FlowPane root = new FlowPane();
		root.setPrefSize(800, 600);

		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();

		HashMap<String, Integer> map = null;
		try {
			map = file.wordsCounter(words, filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(20)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new))
				.forEach((a, integer) -> series.getData().add(new XYChart.Data<>(a, integer)));
		chart.setTitle("Word Occurrence");
		xAxis.setTickLabelRotation(90);

		chart.getData().addAll(series);

		chart.setLegendVisible(false);

		root.getChildren().addAll(btn, btn2, chart);

		Scene sc = new Scene(root, 500, 500);

		s.setScene(sc);

		s.show();
	}

	public static void main(String args[]) {

		launch(args);

	}

}