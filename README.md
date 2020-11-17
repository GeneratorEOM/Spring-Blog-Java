# SpringMiniProjectJava

> 본 프로젝트는 스프링 학습을 위한 강의 기반 프로젝트

> [참고 강의사이트](https://www.inflearn.com/course/spring-mvc5-project/dashboard)

## 개발 환경
**Front-end**
  * HTML5
  * CSS3
  * BootStrap 4.0
  
**Back-end**
  * Java 11
  * Oracle 11g

**Tools**
  * Eclipse (Spring Framework 5.0)
  * apache/tomcat9.0
  * SQL developer
 
 
## 시작하기
* MVC 란?
  * MVC 는 Model - VIew - Controller 소프트웨어 엔지니어링에서 사용자 인터페이스와  
비즈니스 로직을 분리하는 데 사용하는 패턴이다.
  * Model 은 비즈니스 계층을 정의하고 Controller 는 애플리케이션 흐름을 관리하며  
View 는 애플리케이션 프리젠테이션 계층을 정의한다.

* 프로젝트 만들기  
![dy](https://user-images.githubusercontent.com/64389409/99224157-21c10480-2829-11eb-8085-2d5c99092084.PNG)
![ma](https://user-images.githubusercontent.com/64389409/99224156-208fd780-2829-11eb-8a1e-1f121485a35d.PNG)  

porm.xml 에 스프링 MVC 사용을 위한 셋팅  
https://mvnrepository.com/ 에서 검색해서 추가한다.  
groupId 로 properties 에서 버전 관리가 가능하다.  

``` xml
<!-- 라이브러리 버전 관리 -->
	<properties>
		<javax.servlet-version>4.0.1</javax.servlet-version>
		<javax.servlet.jsp-version>2.3.3</javax.servlet.jsp-version>
		<javax.servlet.jsp.jstl-version>1.2</javax.servlet.jsp.jstl-version>
		<org.springframework-version>5.2.2.RELEASE</org.springframework-version>
		<!-- <org.springframework-version>4.3.25.RELEASE</org.springframework-version> -->
	</properties>
	
	<!-- 라이브러리 셋팅 -->
	<dependencies>
		<!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
		<!-- 자바 서블릿 API -->
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
			<version>${javax.servlet-version}</version>
			<scope>provided</scope>
		</dependency>
		<!-- https://mvnrepository.com/artifact/javax.servlet.jsp/javax.servlet.jsp-api -->
		<!-- 자바 서버 API -->
		<dependency>
			<groupId>javax.servlet.jsp</groupId>
			<artifactId>javax.servlet.jsp-api</artifactId>
			<version>${javax.servlet.jsp-version}</version>
			<scope>provided</scope>
		</dependency>
		<!-- https://mvnrepository.com/artifact/javax.servlet.jsp.jstl/jstl -->
		<!-- JSTL -->
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>jstl</artifactId>
			<version>${javax.servlet.jsp.jstl-version}</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
		<!-- 스프링 웹 MVC -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-webmvc</artifactId>
			<version>${org.springframework-version}</version>
		</dependency>
	</dependencies>
```
Spring MVC 는 xml 과 Java 클래스 형식으로 설정이 가능하다. 본 프로젝트는 Java 형식으로 설정되었다.  

* config 파일 설정  
  * SpringConfigClass.java : 웹 어플리케이션 실행 시 가장 먼저 실행되게 설정하는 클래스다. 2가지 사용방법이 있다.
    * WebApplicationInitializer 인터페이스 구현
    * AbstractAnnotationConfigDispatcherServletInitializer 클래스 
  * ServletAppContext.java : Spring MVC 프로젝트에 관련된 설정을 하는 클래스다.
  * RootAppContext.java : 사용할 Bean 을 정의하는 클래스다.


  
SpringConfigClass.java  - WebApplicationInitializer 구현
  
``` java
public class SpringConfigClass implements WebApplicationInitializer{
	// 웹 어플리케이션을 실행할 때 WebApplicationInitializer을 구현한 클래스가 있다면 
	// onStartup() 을 먼저 실행하게 된다.
	// web.xml 의 내용을 셋팅한다고 보면 된다.
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		// Spring MVC 프로젝트 설정을 위해 작성하는 클래스의 객체를 생성한다.
		AnnotationConfigWebApplicationContext servletAppContext = new AnnotationConfigWebApplicationContext();
		servletAppContext.register(ServletAppContext.class);
		
		// 요청 발생 시 요청을 처리하는 서블릿을 Dispatcher Servlet 으로 설정한다.
		DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
		ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", dispatcherServlet);
		
		// 부가 설정
		// 모든 요청에 대해 가장 먼저 로딩된다.
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
		
		// Bean 을 정의하는 클래스의 객체를 생성한다.
		AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
		rootAppContext.register(RootAppContext.class);
		
		// 리스너 설정
		ContextLoaderListener listener = new ContextLoaderListener(rootAppContext);
		servletContext.addListener(listener);
		
		// 파라미터 인코딩 설정
		// dispatcher 이름으로 등록된 서블릿이 받아들이는 요청에 대해서 인코딩 필터를 통과시킨다.
		FilterRegistration.Dynamic filter = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
		filter.setInitParameter("encoding", "UTF-8");
		filter.addMappingForServletNames(null, false, "dispatcher");
		
		
	}

}

```    
  
SpringConfigClass.java  - AbstractAnnotationConfigDispatcherServletInitializer 상속

```java

public class SpringConfigClass extends AbstractAnnotationConfigDispatcherServletInitializer {
	// DispatcherServlet 에 매핑할 요청 주소를 셋팅한다.
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	// Spring MVC 프로젝트 설정을 위한 클래스를 지정한다.
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { ServletAppContext.class };
	}

	// 프로젝트에서 사용할 Bean 들을 정의하기 위한 클래스를 지정한다.
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { RootAppContext.class };
	}
	
	// 파라미터 인코딩 필터 설정
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		return new Filter[] { encodingFilter };
	}
}
```
  
ServletAppContext.java
  
```java
// Spring MVC 프로젝트에 관련된 설정을 하는 클래스
@Configuration
// Controller 어노테이션이 셋팅되어 있는 클래스를 Controller 로 등록한다.
@EnableWebMvc
// 스캔할 패키지를 지정한다.
@ComponentScan("kr.co.blog.controller")
public class ServletAppContext implements WebMvcConfigurer {
	
	// Controller 의 메서드가 반한하는 jsp 의 이름 앞뒤에 경로와 확장자를 붙여주도록 설정한다.
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		
		WebMvcConfigurer.super.configureViewResolvers(registry);
		registry.jsp("/WEB-INF/views/", ".jsp");
	}

	// 정적파일의 경로를 매핑한다.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/**").addResourceLocations("/resources/");
	}

}
```  
RootAppContext.java  
```java
// 프로젝트 작업 시 사용할 Bean 을 정의하는 클래스
@Configuration
public class RootAppContext {
	
}
```





## 핵심 기능
### myBatis

#### 마이바티스란?  
마이바티스는 개발자가 지정한 SQL, 저장프로시저 그리고 몇가지 고급 매핑을 지원하는 퍼시스턴스 프레임워크이다.  
JDBC로 처리하는 상당부분의 코드와 파라미터 설정및 결과 매핑을 대신해준다.   
데이터베이스 레코드에 원시타입과 Map 인터페이스 그리고 자바 POJO 를 설정해서 매핑하기 위해 XML과 애노테이션을 사용할 수 있다.  

...   
  
위는 마이바티스에서 소개글을 가져와봤다.  
아마도 JDBC, DBCP 사용할 때 connection, preparedstatement, resultset 객체들을 생성해서    
사용하고 해제하는 번거로운 작업들이 필요없지 않을까 생각해본다.
  
세팅해보자.  
porm.xml 에 추가    
``` xml
<!-- oracle DB 연동 셋팅 -->	
<!-- https://mvnrepository.com/artifact/org.springframework/spring-jdbc -->
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-jdbc</artifactId>
  <version>${org.springframework-version}</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.apache.commons/commons-dbcp2 -->
<dependency>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-dbcp2</artifactId>
  <version>2.8.0</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis</artifactId>
  <version>3.5.6</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.mybatis/mybatis-spring -->
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis-spring</artifactId>
  <version>2.0.5</version>
</dependency>
```  

SevletAppContext.java 에 mybatis 설정을 추가한다.
db 접속정보는 properties 파일을 만들어서 따로 저장해두고 불러와서 사용한다.

```java
// MyBatis 설정
// 프로퍼티파일 불러오기
@PropertySource("/WEB-INF/properties/db.properties")
...
// 프로퍼티파일 안에 값들 입력하기
@Value("${db.classname}")
private String db_classname;
...
// 데이터베이스 접속정보 관리
@Bean
public BasicDataSource dataSource() {
	BasicDataSource source = new BasicDataSource();
	source.setDriverClassName(db_classname);
	source.setUrl(db_url);
	source.setUsername(db_username);
	source.setPassword(db_password);	
	return source;
}
// 쿼리문과 접속 관리하는 객체
@Bean
public SqlSessionFactory factory(BasicDataSource source) throws Exception {
	SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
	factoryBean.setDataSource(source);
  SqlSessionFactory factory = factoryBean.getObject();
	return factory;
}	
// 쿼리문 실행을 위한 객체
@Bean
public MapperFactoryBean<BoardMapper> getBoardMapper(SqlSessionFactory factory) throws Exception {
	MapperFactoryBean<BoardMapper> factoryBean = new MapperFactoryBean<BoardMapper>(BoardMapper.class);
	factoryBean.setSqlSessionFactory(factory);
	return factoryBean;
}
```


### 폼 태그 라이브러리  
form 태그 라이브러리를 사용하면 폼에 데이터를 바인딩하거나 에러메세지 처리등을 간편하게 할 수 있다.

``` jsp
<!-- 로그인 폼 -->
<form:form action="/login" method="post" modelAttribute="LoginUserBean">
<div class="form-group">
  <form:label path="id">아이디</form:label>
  <form:input path="id" class="form-control" />
  <form:errors path="id"/>
</div>
<div class="form-group">
  <form:label path="pw">비밀번호</form:label>
  <form:password path="pw" class="form-control" />
  <form:errors path="pw"/>
</div>
<div class="form-group">
  <form:button type="submit" class="btn">로그인</form:button>
</div>
</form:form>
```
modelAttribute 설정을 하면 같은 이름의 Bean 데이터가 자동으로 바인딩 되며  
<form:errors> 를 통해 입력값에 대한 에러메세지를 보여주기에도 편리하다.

### 서버단 로직
서버단 로직은 기본적으로 순서는 아래와 같다.  
1. Dispatcher Servlet 이 클라이언트 요청을 받는다.
2. HandlerMapping 에 의해 실행할 컨트롤러를 찾는다.
3. Controller method -> Service -> Database Access Object 순으로 로직이 처리되며     
   결과값인 Model 정보가 다시 Dispatcher Servlet 로 반환된다.
4. ViewResolver 가 알맞은 JSP 파일을 찾는다.
5. View 가 Model 정보을 토대로 클라이언트에게 결과값을 반환한다.

간단한 게시판 CRUD 로 알아보자.

먼저 ContentBean.java 를 만든다.
JSP form 에 등록된 path 명과 Bean 에 등록된 변수명을 같게 설정하면 스프링이 자동으로 값들을 넣어준다. 
```java
public class ContentBean {
  private int content_idx;
  private String content_subject;
  private String content_text;
  ...
  // get, set 메서드 정의
}
```

```jsp
<!-- board.jsp -->
<form:form action="url" method="post" modelAttribute="name">
<!-- 게시글 입력정보들이 담긴다 -->
</form:form>
```
클라이언트에서 url 요청한다.
```java
// 우리가 주석을 달듯이 스프링에게 Controller 라고 명시해주는 어노테이션
@Controller
public class BoardController {
  // 스프링이 자동으로 서비스 객체를 주입해준다.
  @Autowired
  private BoardService boardService; 
  // CREATE
  // JSP form 에서 modelAttriute 속성의 이름을 @ModelAttribute(name="") 으로 맞춰주면 
  // 자동으로 Bean 객체에 입력정보를 주입 받아온다.  
  @GetMapping("/write")
    public String write(@ModelAttribute("writeContentBean") ContentBean writeContentBean) {			
      boardService.addContentInfo(writeContentBean);
      // 포워딩 되는 JSP
      return "board/write";
  }
  // READ
  // @RequestParam(name="")은 파라미터에서 같은 이름의 데이터를 주입 받아온다.
  @GetMapping("/read")
  public String read(@RequestParam("content_idx") int content_idx, Model model) {
    // 게시글 정보를 가져와서 담는다.
    ContentBean contentInfo = boardService.getContentInfo(content_idx);
    // 모델에 담아서 리턴한다.
    model.addAttribute("contentInfo", contentInfo);
    return "board/read";
  }
  // UPDATE
  @PostMapping("/modify")
  public String modify(@ModelAttribute("modifyContentBean") ContentBean modifyContentBean) {
    // 수정 정보를 담아서 서비스단 호출한다.
    boardService.modifyContentInfo(modifyContentBean);
    return "board/modify";
  }
  // DELETE
  @GetMapping("/delete")
  public String delete(@RequestParam("content_idx") int content_idx) {
    boardService.deleteContentInfo(content_idx);	
    return "board/delete";
  }
}
```  
매핑되는 메서드가 실행고 서비스단을 호출한다.  

```java
// BoardService.java
// 파라미터로 어떠한 계산과정이나 로직이 필요한 경우는 Service 단에서 처리하는 것 같다.
// 우리가 주석을 달듯이 스프링에게 Service 라고 명시해주는 어노테이션
@Service
public class BoardService {
  // 스프링이 자동으로 DAO 객체를 주입해준다.
  @Autowired
  private BoardDAO boardDAO;
  // CREATE
  public void addContentInfo(ContentBean writeContentBean) {		
    boardDAO.addContentInfo(writeContentBean);
  }
  // READ
  public void getContentInfo(int content_idx) {		
    boardDAO.getContentInfo(content_idx);
  }
  // UPDATE
  public void modifyContentInfo(ContentBean modifyContentBean) {		
    boardDAO.modifyContentInfo(modifyContentBean);
  }
  // DELETE
  public void deleteContentInfo(int content_idx) {		
    boardDAO.deleteContentInfo(content_idx);
  }
}
``` 
DAO 를 호출한다.
```java
// BoardDAO.java
// DAO 단은 디비와 연동하는 클래스다.
@Autowired
private BoardMapper boardMapper;
// CREATE
public void addContentInfo(ContentBean writeContentBean) {
  boardMapper.addContentInfo(writeContentBean);		
}
// READ
public void getContentInfo(int content_idx) {
  boardMapper.getContentInfo(content_idx);		
}
// UPDATE
public void modifyContentInfo(ContentBean modifyContentBean) {
  boardMapper.modifyContentInfo(modifyContentBean);		
}
// DELETE
public void deleteContentInfo(int content_idx) {
  boardMapper.deleteContentInfo(content_idx);		
}
```
Mapper 를 호출한다.  
쿼리문만 적어주면 mybatis 에서 필요한 작업들을 자동으로 처리해주는듯 하다.  

```java
// BoardMapper.java
// 인터페이스로 생성된다.
// 쿼리문을 작성한다.
public interface BoardMapper {
  // writeContentBean 에 담긴 정보로 INSERT
  @Insert("INSERT ... ")
  void addContentInfo(ContentBean writeContentBean);
  // 게시글을 READ
  @Select("SELECT ... ")
  ContentBean getContentList(int content_idx);
  // modifyContentBean 에 담긴 수정 정보로 UPDATE
  @Update("UPDATE ... ")
  void modifyContentInfo(ContentBean modifyContentBean);
  // 글번호로 DELETE 
  @Delete("DELETE ...")
  void deleteContentInfo(int content_idx);
}

```

### 인터셉터를 이용한 로그인 처리
#### 인터셉터란?
인터셉터란 컨트롤러에 들어오는 요청을 가로채는 역할을 한다.   
preHandle(), postHandle(), afterCompletion() 3가지 메서드가 있다.
  
Interceptor를 구현하는 방법은 2가지가 있다
 1. HandlerInterceptor 인터페이스를 구현
 2. HandlerInterceptorAdapter 클래스를 상속 받는 방법 
   
각 메서드의 반환값이 true 이면 핸들러의 다음 체인이 실행되지만 false 이면 중단하고 남은 인터셉터와 컨트롤러가 실행되지 않습니다.
  
```java
// 1번 방법으로 구현
public class CheckLoginInterceptor implements HandlerInterceptor{
  private UserBean loginUserBean;
  // 인터셉터는 DI 가 안되므로 생성자를 통해서 주입 받는다.
  public CheckLoginInterceptor(UserBean loginUserBean) {
    this.loginUserBean = loginUserBean;
	}
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
  // 컨트롤러가 호출되기 전에 실행된다.
  // 로그인이 되어 있지 않으면
  if(loginUserBean.isUserLogin() == false) {
    String contextPath = request.getContextPath();
    // 리다이렉트 시키고
    response.sendRedirect(contextPath + "/user/not_login");
    // return false 로 다음 과정들은 중단시킨다.
    return false;
  }
  // 로그인 되어 있으면 return true 다음 과정이 실행된다.
  return true;
  }
  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    // 컨트롤러가 호출되고 난 후에 실행된다.
  }
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    // 컨트롤러의 처리가 끝나고 화면처리까지 모두 끝나면 실행되는 메서드이다.
  }	
}
```
  
만든 인터셉터를 ServletAppContext.java 에 등록해준다.
  
```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
WebMvcConfigurer.super.addInterceptors(registry);
  // 만든 인터셉터 객체를 생성한다.
  CheckLoginInterceptor checkLoginInterceptor = new CheckLoginInterceptor(loginUserBean);
  // 생성한 객체를 등록한다.
  InterceptorRegistration reg = registry.addInterceptor(checkLoginInterceptor);
  // 아래 패턴에 대해서 인터셉터를 모두 실행한다.		
  reg.addPathPatterns("/user/modify", "/user/logout", "/board/*");
  // 예외 패턴을 설정할 수 있다.
  reg.excludePathPatterns("/board/main");
}
```
### 파일업로드 처리  
  
파일처리 설정를 위해 ServletAppContext.java 에 추가한다.
    
```java
// 파일처리를 위한 빈을 등록한다. 
@Bean
public StandardServletMultipartResolver multipartResolver() {
	return new StandardServletMultipartResolver();
}
```
  
enctype="multipart/form-data" 로 변경 
  
```jsp
<!-- enctype="multipart/form-data" -->
<form:form action="url" method="post" modelAttribute="name" enctype="multipart/form-data">
<!-- 게시글 입력정보들이 담긴다 -->
</form:form>
```
  
Bean 객체에  이름을 저장할 변수와 실제 파일을 저장할 변수를 선언한다.
  
```java
// 파일 이름만 디비에 저장
private MultipartFile upload_file;
// 실제 파일 정보 저장
private MultipartFile upload_file;
...
```

```java
private String saveUploadFile(MultipartFile upload_file) {
  // 파일이름 중복 방지를 위해 파일이름에 시간을 더한다.
  String file_name = System.currentTimeMillis() + upload_file.getOriginalFilename();
		
  try {
    // 파일 저장
    upload_file.transferTo(new File(path_upload + "/" + file_name));
  } catch (Exception e) {
    e.printStackTrace();
  }
  return file_name;
}

public void addContentInfo(ContentBean writeContentBean) {

  MultipartFile upload_file = writeContentBean.getUpload_file();
  // 파일이 존재하면 처리		
  if(upload_file.getSize() > 0) {
    // saveUploadFile() 실행해서 파일이름 받아온다.
    String file_name = saveUploadFile(upload_file);
    writeContentBean.setContent_file(file_name);
  }
}
```  
여기까진 동일한데 AbstractAnnotationConfigDispatcherServletInitializer 을 상속받아서   
SpringConfigClass.java 를 설정했다면 오류가 난다.  
잘모르겠지만 구글링해서 다른 방법을 찾았다.    
아래 2개 라이브러리를 추가하고 설정을 조금 수정하자.    
``` xml
<dependency>
  <groupId>commons-fileupload</groupId>
  <artifactId>commons-fileupload</artifactId>
  <version>1.2.1</version>
</dependency>
<dependency>
  <groupId>commons-io</groupId>
  <artifactId>commons-io</artifactId>
  <version>1.4</version>
</dependency>
```
```java	
// 파일처리
// StandardServletMultipartResolver 를 삭제하고
// CommonsMultipartResolver 를 추가한다.
@Bean
public CommonsMultipartResolver multipartResolver() {
  CommonsMultipartResolver resolver = new CommonsMultipartResolver();
  resolver.setMaxUploadSize(52428800);
  return resolver;
}
```
  
AbstractAnnotationConfigDispatcherServletInitializer 을 상속받았다면 아래 코드만 추가하면 된다.
  
```java
// 파일 처리
@Override
protected void customizeRegistration(Dynamic registration) {
  super.customizeRegistration(registration);
  // ( 임시폴더 주소,  업로드 파일의 최대용량, 파일데이터를 포함 전체 요청정보의 최대용량, 임계값 0 )
  MultipartConfigElement config1 = new MultipartConfigElement(null, 52428800, 524288000, 0);
  registration.setMultipartConfig(config1);
}
```











