javac -d ./bin/ -cp ./bin:lib/* ./src/model/*.java ./src/view/File/*.java ./src/view/Panel/*.java ./src/view/Table/*.java ./src/view/*.java
java --enable-preview --class-path bin:./lib/* view.Bilancio

