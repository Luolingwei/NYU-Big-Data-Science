package Tasks;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Visualization {

    public String name;
    public double[][] data;
    public double[][] centers;
    public int[] clusters;
    public JFreeChart chart;

    public Visualization(String name, double[][] data) {
        this(name, data,null, null);
    }

    public Visualization(String name, double[][] data, double[][] centers, int[] clusters) {
        this.name = name;
        this.data = data;
        this.centers = centers;
        this.clusters = clusters;

    }

    public JFreeChart createChart (String name) {
        XYDataset dataset = createDataset();
        chart = ChartFactory.createScatterPlot(
                "Visualization of the " + name, null, null,
                dataset);
        XYItemLabelGenerator labels = (data, series, item) -> {
            if (data.getSeriesCount() == 1)
                return null;
            if (series == data.getSeriesCount() - 1)
                return "Center " + (item + 1);
            return null;
        };
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(192,192,192));
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setBaseItemLabelGenerator(labels);
        renderer.setBaseItemLabelsVisible(true);
        return chart;
    }

    public XYDataset createDataset () {
        XYSeriesCollection dataset = new XYSeriesCollection();
        List<XYSeries> series = new ArrayList<>();
        if (clusters == null) {
            XYSeries rawData = new XYSeries("Raw Tf-Idf Representation of Documents");
            for (int i = 0; i < data.length; i++)
                rawData.add(data[i][0], data[i][1]);
            dataset.addSeries(rawData);
        }
        else {
            Arrays.sort(clusters);
            int k = clusters[clusters.length - 1] + 1;
            for (int i = 0; i < k; i++)
                series.add(new XYSeries("Cluster" + (i + 1)));
            for (int i = 0; i < data.length; i++)
                series.get(clusters[i]).add(data[i][0], data[i][1]);
            for (int i = 0; i < k; i++)
                dataset.addSeries(series.get(i));
            XYSeries centersSeries = new XYSeries("centers");
            for (int i = 0; i < centers.length; i++)
                centersSeries.add(centers[i][0], centers[i][1]);
            dataset.addSeries(centersSeries);
        }
        return dataset;
    }

    public void createPlot () throws IOException {
        chart = createChart(name);
        ChartUtilities.saveChartAsPNG(new File(name + ".png"), chart, 1000, 600);
        System.out.println("The generated chart has been saved as \"" + name + ".png\" in current path");
    }

}
