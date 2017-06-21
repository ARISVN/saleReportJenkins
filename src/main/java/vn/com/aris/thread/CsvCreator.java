package vn.com.aris.thread;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

/**
 * Using jackson-dataformat-csv to create CSV file out of POJOs (bean class).
 * Use with maven-dependency:
 * <dependency>
 * <groupId>com.fasterxml.jackson.dataformat</groupId>
 * <artifactId>jackson-dataformat-csv</artifactId>
 * <version>2.3.3</version>
 * </dependency>
 * https://github.com/FasterXML/jackson-dataformat-csv
 */
public class CsvCreator {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {

        // POJO (bean class)
        @JsonPropertyOrder({"name", "age"})
        class User {
            public String name;
            public int age;
            public User() {
            }
            public String getName() {
                return name;
            }
            public void setName(String name) {
                this.name = name;
            }
            public int getAge() {
                return age;
            }
            public void setAge(int age) {
                this.age = age;
            }
        }

        // define objects
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
        	 User user = new User();
        	 user.name = "name" + (i + 1);
        	 user.age = 30 + i;
        	 users.add(user);
		}
       
        // create mapper and schema
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(User.class).withHeader();
        schema = schema.withColumnSeparator(',');

        // output writer
        ObjectWriter myObjectWriter = mapper.writer(schema);
        File tempFile = new File("D:\\users.csv");
        FileOutputStream tempFileOutputStream = new FileOutputStream(tempFile);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 1024);
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, "UTF-8");
        myObjectWriter.writeValue(writerOutputStream, users);
    }
}