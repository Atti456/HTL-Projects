package Address.view;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import Address.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

public class BirthdayStatisticsController {

	@FXML
	private BarChart<String, Integer> barChart;

	@FXML
	private CategoryAxis xAxis;

	private ObservableList<String> monthNames = FXCollections.observableArrayList();

	@FXML
	private void initialize() {
		// Ein Array mit den deutschen Monatsnamen.
		String[] months = DateFormatSymbols.getInstance(Locale.GERMAN).getMonths();
		// Konvertiert sie zu einer Liste und fügt sie zur ObservableList monthNames
		// hinzu.
		monthNames.addAll(Arrays.asList(months));

		// Die Monatsnamen werden als KAtegorie für die x-Achse gesetzt.
		xAxis.setCategories(monthNames);
	}

	public void setPersonData(List<Person> persons) {
		// Zählt die Anzahl der Personen die im selben Monat Geburtstag haben.
		int[] monthCounter = new int[12];
		for (Person p : persons) {
			int month = p.getBirthday().getMonthValue() - 1;
			monthCounter[month]++;
		}

		XYChart.Series<String, Integer> series = new XYChart.Series<>();

		// Erstellt die x-Achse und befüllt sie mit den Monatsnamen.
		for (int i = 0; i < monthCounter.length; i++) {
			series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
		}

		barChart.getData().add(series);
	}
}
