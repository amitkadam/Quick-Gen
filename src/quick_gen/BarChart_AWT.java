package quick_gen;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.ui.ApplicationFrame; 

import java.util.ArrayList;
import java.util.Map;

public class BarChart_AWT extends ApplicationFrame
{
   public BarChart_AWT( String applicationTitle , String chartTitle, Map<String, Map<String, ArrayList>> resultMap)
   {
      super( applicationTitle );        
      JFreeChart barChart = ChartFactory.createBarChart(
         chartTitle,           
         "Symbols",            
         "Result",            
         createDataset(resultMap),          
         PlotOrientation.VERTICAL,           
         true, true, false);
         
      ChartPanel chartPanel = new ChartPanel( barChart );        
      chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );        
      setContentPane( chartPanel ); 
   }
   private CategoryDataset createDataset(Map<String, Map<String, ArrayList>> resultMap)
   {
      final String pass = "PASS";        
      final String fail = "FAIL";        
      final DefaultCategoryDataset dataset = 
      new DefaultCategoryDataset( );  

      for (Map.Entry entry : resultMap.entrySet()) {
    	  @SuppressWarnings("unchecked")
    	  Map<String, ArrayList> d = (Map<String, ArrayList>) entry.getValue();
    	  ArrayList<Integer> p = (ArrayList) d.get("pass");
    	  ArrayList<Integer> f = (ArrayList) d.get("failed");
    	  dataset.addValue(f.size() , fail , entry.getKey().toString());
    	  dataset.addValue(p.size(), pass , entry.getKey().toString());
		}
      return dataset; 
   }
}
