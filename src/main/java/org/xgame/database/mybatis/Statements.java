package org.xgame.database.mybatis;

/**
 * @Name: Statements.class
 * @Description: //
 * @Create: DerekWu on 2018/9/1 18:03
 * @Version: V1.0
 */
public class Statements {

    private String insertStatement;
    private String updateStatement; 
    private String deleteStatement;
    private String selectOneStatement;

    private Statements(String namespace, Class t) {
        String className = t.getSimpleName();
        this.insertStatement = (namespace + ".insert" + className);
        this.updateStatement = (namespace + ".update" + className);
        this.deleteStatement = (namespace + ".delete" + className + "ById");
        this.selectOneStatement = (namespace + ".selectOne" + className + "ById");
    }

    public static Statements instance(Class t) {
        return new Statements(t.getSimpleName(), t);
    }

    public static Statements instance(String namespace, Class t) {
        return new Statements(namespace, t);
    }

    public String getInsertStatement() {
        return insertStatement;
    }

    public void setInsertStatement(String insertStatement) {
        this.insertStatement = insertStatement;
    }

    public String getUpdateStatement() {
        return updateStatement;
    }

    public void setUpdateStatement(String updateStatement) {
        this.updateStatement = updateStatement;
    }

    public String getDeleteStatement() {
        return deleteStatement;
    }

    public void setDeleteStatement(String deleteStatement) {
        this.deleteStatement = deleteStatement;
    }

    public String getSelectOneStatement() {
        return selectOneStatement;
    }

    public void setSelectOneStatement(String selectOneStatement) {
        this.selectOneStatement = selectOneStatement;
    }
}
