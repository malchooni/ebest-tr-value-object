package name.malchooni.conversion.ebest.res;

import java.util.*;

/**
 * RES DESCRIPTION value object
 */
public class ResFileData {

    public static final int RES_TYPE_IDX = 0;
    public static final int RES_DESCRIPTION_IDX = 1;
    public static final String REAL = ".Feed";
    public static final String QUERY = ".Func";

    public static final int DESC_IDX = 0;
    public static final int COLUMN_IDX = 1;
    public static final int TYPE_IDX = 3;
    public static final int SIZE_IDX = 4;

    public static final String DESC = "DESC";
    public static final String COLUMN = "COLUMN";
    public static final String TYPE = "TYPE";
    public static final String SIZE = "SIZE";

    private final Map<String, List<Map<String, String>>> dataMap;

    private final String name;
    private String resType;
    private String description;

    public ResFileData(String name) {
        this.name = name;
        this.dataMap = new LinkedHashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, List<Map<String, String>>> getDataMap() {
        return dataMap;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDataBlock(String dataBlockName, List<Map<String, String>> dataBlock) {
        this.dataMap.put(dataBlockName, dataBlock);
    }
}


