## spring-boot-vue demo
A sample TODO application, using a spring-boot backend and a Vue.js frontend


### pre-requisites
- Ensure a JDK 8 is on your path
- start a MongoDB on the standard port (27017), without authentication
  * the simplest is to use Docker: `docker run -p 27017:27017 -d mongo`

If using VS Code:
- install the [Language support for Java by Red Hat extension](https://marketplace.visualstudio.com/items?itemName=redhat.java)
- install the [Debugger for Java extension](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-debug)


### Run from a terminal
From the root directory:
- Execute `./mvnw install && ./mvnw spring-boot:run -pl todo-app`
- Open http://localhost:9090

### Run from VS Code
- Open https://github.com/fbricon/spring-boot-vue/blob/master/todo-app/src/main/java/com/redhat/vscode/demo/TodoApplication.java#L26
- click on the `Run` or `Debug` code lens.
- Open http://localhost:9090
