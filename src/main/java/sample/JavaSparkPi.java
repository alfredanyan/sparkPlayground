/*
 * Copyright (c) Microsoft Corporation
 *
 * All rights reserved.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package sample;

import com.mongodb.Mongo;
import com.mongodb.spark.config.WriteConfig;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.SparkSession;
import com.mongodb.spark.MongoSpark;
import org.codehaus.janino.Java;
import scala.Tuple2;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;

import org.bson.Document;

import java.util.*;

import static java.util.Arrays.asList;

/**
 * Computes an approximation to pi
 * Usage: JavaSparkPi [slices]
 * This is adapted from Apache Spark GitHub: https://github.com/apache/spark/blob/master/examples/src/main/java/org/apache/spark/examples/JavaSparkPi.java
 */
public final class JavaSparkPi {
    public static void main(String[] args) throws Exception {
        SparkConf sparkConf = new SparkConf().setAppName("JavaSparkPi").setMaster("local");
        //JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        String sampleText = "C:\\Users\\alfre\\Sandbox\\Spark\\SparkTest.txt";

  //       use this line if you want to run your application in the cluster
        // SparkConf sparkConf = new SparkConf().setAppName("JavaSparkPi");
        SparkSession spark = SparkSession
                .builder()
                .appName("MongoSparkConnector")
                .config("spark.master", "local")
                .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/sparkDb.sparkInfo")
                .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/sparkDb.sparkInfo")
                .getOrCreate();

        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

        JavaRDD<Document> documents = jsc.parallelize(asList(1,2,3,4,5,6,7,9,10))
                .map(new Function<Integer, Document>() {
                    public Document call(final Integer i) throws Exception {
                        return Document.parse("{test: " + i +" }");
                    }
                });
        Map<String, String> writeOverrides = new HashMap<String, String>();
        writeOverrides.put("collection", "spark");
        writeOverrides.put("writeConcern.w", "majority");
        WriteConfig writeConfig = WriteConfig.create(jsc).withOptions(writeOverrides);
        MongoSpark.save(documents, writeConfig);
        System.out.println("Success");

//        JavaMongoRDD mongoRDD = MongoSpark.load(jsc);
//        if(mongoRDD.isEmpty()){
//            System.out.println("rdd empty");
//        }else {
//            System.out.println("read success");
//        }

//        System.out.println(mongoRDD.count());
//        System.out.println(mongoRDD.first().toString());
//        for(JavaRDD rdd: mongoRDD.collect()){
//    System.out.println(rdd.toString());
//        }
        //System.out.println(mongoRDD.first().toJson());









        // PRINTING
        //DONT DO:
        //rdd.foreach(println) or rdd.map(println)
        //outputs will go to worker stdout
        //DO: to ensure all rdd's are brought to driver. add take() to prevent memory overload on driver
        //take():rdd.collect().foreach(println);








        //IMPLEMENTING INTERFACES FROM SPARK API - Method 2 - as named classes while
        //passing an instance of spark to them

//        class GetLength implements Function<String, Integer> {
//            public Integer call(String s) {
//                return s.length();
//            }
//        }
//        class Sum implements Function2<Integer, Integer, Integer> {
//            public Integer call(Integer a, Integer b) {
//                return a + b;
//            }
//        }
//
//        JavaRDD<String> lines = jsc.textFile(sampleText);
//        JavaRDD<Integer> lineLengths = lines.map( new GetLength ());
//        int totalLength = lineLengths.reduce(new Sum());
//        System.out.println("Total length is: " + totalLength);
//
//



//        //IMPLEMENTING INTERFACES FROM SPARK API - Method 1 - as Anonymous Inline Class
        // anonymous classes can access variables in enclosing scope as long as
        //these variables are marked final
//        JavaRDD<String> lines = jsc.textFile(sampleText);
//        JavaRDD<Integer> lineLengths = lines.map(new Function<String, Integer> () { public Integer call(String s) {return s.length(); }});
//        int totalLength = lineLengths.reduce(new Function2<Integer, Integer, Integer>() { public Integer call(Integer a, Integer b) { return a + b;}});
//        System.out.println("Total length is: " + totalLength);





//// CONSUMING TEXTFILE - INTO RDD AND COUNT NUMBER OF LINES AND PRINT EACH
//        JavaRDD<String> rddFromText = jsc.textFile(sampleText);
//        //adding up size of all lines
//        rddFromText.map(s -> s.length()).reduce((a, b) -> a + b);
//        System.out.println("Number of lines is " + rddFromText.count());
//        for(String lines: rddFromText.collect()){
//            System.out.println(lines);
//        }






        //PARALLELIZED COLLECTION
//        List<Integer> data = Arrays.asList(1,2,3,4,5);
//        JavaRDD<Integer> distData = jsc.parallelize(data);
//        distData.collect();
//        for(Integer number: distData.collect()){
//            System.out.println(number);
//        }








        //WORDCOUNT EXAMPLE - ISSUE WITH saveAsTextFile method
//        JavaRDD<String> textFile = jsc.textFile(sampleText);
//        JavaPairRDD<String, Integer> counts = textFile
//                .flatMap(s -> Arrays.asList(s.split(" ")).iterator())
//                .mapToPair(word -> new Tuple2<>(word, 1))
//                .reduceByKey((a, b) -> a + b);
////        counts.collect();
//        counts.saveAsTextFile("C:\\Users\\alfre\\Sandbox\\Spark\\SparkTestOutput33");


//
//
//        int slices = (args.length == 1) ? Integer.parseInt(args[0]) : 2;
//        int n = 100000 * slices;
//        List<Integer> l = new ArrayList<Integer>(n);
//        for (int i = 0; i < n; i++) {
//            l.add(i);
//        }
//
//        JavaRDD<Integer> dataSet = jsc.parallelize(l, slices);
//
//        int count = dataSet.map(new Function<Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer) {
//                double x = Math.random() * 2 - 1;
//                double y = Math.random() * 2 - 1;
//                return (x * x + y * y < 1) ? 1 : 0;
//            }
//        }).reduce(new Function2<Integer, Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer, Integer integer2) {
//                return integer + integer2;
//            }
//        });
//
//        System.out.println("Pi is roughly " + 4.0 * count / n);

        jsc.stop();
    }
}
