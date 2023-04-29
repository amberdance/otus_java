package ru.otus.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EntityReflectionMetaTest {
    private static final ClientEntity client = new ClientEntity("1", "Client");
    private static final ManagerEntity manager = new ManagerEntity(1L, "Manager", "TestParam");
    private static final EntityReflectionMeta clientReflection = new EntityReflectionMeta(client.getClass());
    private static final EntityReflectionMeta managerReflection = new EntityReflectionMeta(manager.getClass());
    private static final EntityReflectionMeta testEntityReflection = new EntityReflectionMeta(TestEntity.class);

    @Test
    @DisplayName("Проверка на соответствие по количеству полей")
    void testClientModelFields() {
        assertEquals(2, clientReflection.getFields().size());
        assertEquals(3, managerReflection.getFields().size());
    }

    @Test
    @DisplayName("Проверка класса без полей")
    void testEmptyEntity() {
        assertEquals(0, testEntityReflection.getFields().size());
    }

    @Test
    @DisplayName("Возвращаются все поля кроме аннотированных @Id")
    void testClientFieldsWithoutId() {
        assertEquals(1, clientReflection.getFieldsExclude("id").size());
        assertEquals(2, managerReflection.getFieldsExclude("identifier").size());
    }

    @Test
    @DisplayName("С аннотацией @Table должно вернуть название из параметра name")
    void testGetTableNameWithTableAnnotation() {
        assertEquals("clients", clientReflection.getTableName());
        assertEquals("managers", managerReflection.getTableName());
    }

    @Test
    @DisplayName("Без аннотации @Table должно вернуть название класса в lowercase")
    void testEmptyEntityTableNameWithoutTableAnnotation() {
        assertEquals("testentity", testEntityReflection.getTableName());
    }

    @Test
    @DisplayName("Проверка поля с анотацией @Id")
    void testIdFieldIsCorrect() {
        assertEquals("id", clientReflection.getIdField().getName());
        assertEquals("identifier", managerReflection.getIdField().getName());
    }


    @Test
    @DisplayName("Выбрасывается npe, если нет полей с аннотацией @Id")
    void testClassWithoutIdAnnotation() {
        assertThrows(NullPointerException.class, testEntityReflection::getIdField);
    }

    @Test
    @DisplayName("Получение конструктора с заданными типами параметров")
    void testGetConstructorOnClient() throws NoSuchMethodException {
        assertEquals(true, ClientEntity.class.getConstructor(String.class, String.class).equals(clientReflection.getConstructor(String.class, String.class)));
        assertEquals(true, ManagerEntity.class.getConstructor(long.class, String.class, String.class).equals(managerReflection.getConstructor(long.class, String.class, String.class)));
    }


    @Test
    @DisplayName("Значение приватных полей id соответствуют заданным при инициализации классов ManagerEntity, ClientEntity")
    void testGetIdFieldValue() throws IllegalAccessException {
        assertEquals(client.getId(), clientReflection.getIdFieldValue(clientReflection.getIdField(), client));
        assertEquals(manager.getId(), managerReflection.getIdFieldValue(managerReflection.getIdField(), manager));

    }
}