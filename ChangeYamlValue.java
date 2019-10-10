import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
 
/**  
 * @author cayley
 * @time 2019-10-10 11:48:00
 */  
public class ChangeYamlValue {

	public static void main(String[] args) throws Exception {
			Yaml yaml = new Yaml();
			File dumpFile = new File("test.yaml");
			Map<String, String> map =(Map<String, String>)yaml.load(new FileInputStream(dumpFile));
			handleMap(map);
			// 写入Yaml
			DumperOptions dumperOptions = new DumperOptions();
			dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			Yaml yaml1 = new Yaml(dumperOptions);
			File df = new File("/Users/cayley/Downloads/Score.yaml");
			FileWriter fileWriter = new FileWriter(df);
			yaml1.dump(map, fileWriter);
			fileWriter.close();

	}

	public static void handleMap(Map map){
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			Object val = entry.getValue();

			if (val instanceof Map) {
				handleMap((Map) val);
			} else {
				if("image".equals(key)){
					map.put(key,"gcr.azk8s.cn/google_containers/"+val);
				}
			}
		}
	}

}
