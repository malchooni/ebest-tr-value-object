package name.malchooni.conversion.ebest;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import name.malchooni.conversion.Property;
import name.malchooni.conversion.ebest.output.JavaFileTemplate;
import name.malchooni.conversion.ebest.output.vo.TemplateData;
import name.malchooni.conversion.ebest.res.ResFileData;
import name.malchooni.conversion.util.FileHelper;

/**
 * Res Map to create file
 */
public class Convertor {

    private static final String REPEAT = "_repeat";
    private final Property property;

    public Convertor(Property property) {
        this.property = property;
    }

    /**
     * res map 데이터 -> 파일로 변환
     */
    public void convertMessage(Map<String, ResFileData> resMap) throws Exception {

        String[] packageArr = this.property.getPackagePrefixName().split("\\.");
        File outputRootPath = new File(property.getOutputRootPath());
        File parentPath = FileHelper.createDirectory(outputRootPath, packageArr, 0);

        for (Map.Entry<String, ResFileData> elem : resMap.entrySet()) {

            TemplateData requestFile = new TemplateData();
            TemplateData responseFile = new TemplateData();

            String trName = elem.getKey();
            ResFileData resFileData = elem.getValue();

            for (Map.Entry<String, List<Map<String, String>>> childElem : resFileData.getDataMap().entrySet()) {
                String blockName = childElem.getKey();
                List<Map<String, String>> columnList = childElem.getValue();

                // file output
                TemplateData blockData = this.convertBlockData(trName, blockName, columnList);
                this.createJavaFile(parentPath, blockData);

                Map<String, String> blockColumn = this.createBlockColumnMap(blockName);
                if (blockName.toLowerCase().contains("inblock")) {
                    requestFile.putColumn(blockColumn);
                    requestFile.setDescription(resFileData.getDescription() + " 요청");
                } else if (blockName.toLowerCase().contains("outblock")) {
                    if (blockName.contains(REPEAT)) {
                        responseFile.putImport("java.util.List");
                    }
                    responseFile.putColumn(blockColumn);
                    responseFile.setDescription(resFileData.getDescription() + " 응답");
                }
            }
            requestFile.setPackagePrefix(this.property.getPackagePrefixName());
            requestFile.setClassName(trName + "Request");
            requestFile.setTrName(trName);
            this.createJavaFile(parentPath, requestFile);

            responseFile.setPackagePrefix(this.property.getPackagePrefixName());
            responseFile.setClassName(trName + "Response");
            responseFile.setTrName(trName);
            this.putResponseCommonColumns(responseFile);
            this.createJavaFile(parentPath, responseFile);
        }
    }

    /**
     * data to TemplateData
     */
    private TemplateData convertBlockData(String trName, String blockName, List<Map<String, String>> columnList) {
        TemplateData blockData = new TemplateData();
        blockData.setPackagePrefix(this.property.getPackagePrefixName());
        blockData.setTrName(trName);
        if (blockName.contains(REPEAT)) {
            blockData.setClassName(blockName.substring(0, blockName.indexOf(REPEAT)));
        } else {
            blockData.setClassName(blockName);
        }

        blockData.setColumns(columnList);
        return blockData;
    }

    /**
     * 자바 파일 생성
     */
    private void createJavaFile(File parentPath, TemplateData blockData) throws Exception {
        JavaFileTemplate newFile = new JavaFileTemplate(blockData);
        newFile.createFile(parentPath);
    }

    /**
     * create block column map
     */
    private Map<String, String> createBlockColumnMap(String blockName) {

        String type = blockName.substring(0, 1).toUpperCase() + blockName.substring(1);
        if (blockName.contains(REPEAT)) {
            blockName = blockName.substring(0, type.indexOf(REPEAT));
            type = "List<" + type.substring(0, type.indexOf(REPEAT)) + ">";
        }

        Map<String, String> blockColumn = new LinkedHashMap<>();
        blockColumn.put(ResFileData.COLUMN, blockName);
        blockColumn.put(ResFileData.TYPE, type);

        return blockColumn;
    }

    /**
     * 응답 공통 컬럼 삽입
     */
    private void putResponseCommonColumns(TemplateData responseFile) {
        Map<String, String> rsp_cd = new LinkedHashMap<>();
        rsp_cd.put(ResFileData.COLUMN, "rsp_cd");
        rsp_cd.put(ResFileData.TYPE, "String");
        responseFile.putColumn(rsp_cd);

        Map<String, String> rsp_msg = new LinkedHashMap<>();
        rsp_msg.put(ResFileData.COLUMN, "rsp_msg");
        rsp_msg.put(ResFileData.TYPE, "String");
        responseFile.putColumn(rsp_msg);
    }
}
