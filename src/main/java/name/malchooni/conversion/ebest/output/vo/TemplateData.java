package name.malchooni.conversion.ebest.output.vo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 생성 파일 정보 VO
 */
public class TemplateData {

    // 패키지명
    private String packagePrefix;
    // 설명
    private String description;
    // tr_id
    private String trName;
    // class name
    private String className;
    // import 목록
    private final List<String> imports;
    // column 목록
    private List<Map<String, String>> columns;

    public TemplateData() {
        this.imports = new LinkedList<>();
        this.columns = new LinkedList<>();
    }

    public void putColumn(Map<String, String> column) {
        this.columns.add(column);
    }

    public void putImport(String importStr) {
        this.imports.add(importStr);
    }

    public void setPackagePrefix(String packagePrefix) {
        this.packagePrefix = packagePrefix;
    }

    public void setTrName(String trName) {
        this.trName = trName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setColumns(List<Map<String, String>> columns) {
        this.columns = columns;
    }

    public String getPackagePrefix() {
        return packagePrefix;
    }

    public String getTrName() {
        return trName;
    }

    public String getClassName() {
        return className;
    }

    public List<Map<String, String>> getColumns() {
        return columns;
    }

    public List<String> getImports() {
        return imports;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
