javac GenericLinkedList/*.java
javac MapItems/*.java
javac VoiceInput/*.java
javac RMIEngine/*.java
cd RMIEngine
rmic RMIEngine.ServerInterfaceImpl
cd ..

javac *.java
rmic CMA
