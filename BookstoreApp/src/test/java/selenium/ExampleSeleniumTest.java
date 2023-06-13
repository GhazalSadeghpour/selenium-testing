package selenium;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;


import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

class ExampleSeleniumTest {

  static Process server;
  private WebDriver driver;

  @BeforeAll
  public static void setUpBeforeClass() throws Exception {
    ProcessBuilder pb = new ProcessBuilder("java", "-jar", "bookstore5.jar");
    server = pb.start();
  }

  @BeforeEach
  void setUp() {
    // Pick your browser
    // driver = new FirefoxDriver();
    // driver = new SafariDriver();
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();

    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    driver.get("http://localhost:8080/");
    // wait to make sure Selenium is done loading the page
    WebDriverWait wait = new WebDriverWait(driver, 60);
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")));
  }

  @AfterEach
  public void tearDown() {
    driver.close();
  }

  @AfterAll
  public static void tearDownAfterClass() throws Exception {
    server.destroy();
  }


  @Test
  void test1() {
    WebElement element = driver.findElement(By.id("title"));
    String expected = "YAMAZONE BookStore";
    String actual = element.getText();
    assertEquals(expected, actual);
  }

  @Test
  public void test2() {
    WebElement welcome = driver.findElement(By.cssSelector("p"));
    String expected = "Welcome";
    String actual = welcome.getText();
    assertEquals(expected, getWords(actual)[0]);
    WebElement langSelector = driver.findElement(By.id("locales"));
    langSelector.click();
    WebElement frSelector = driver.findElement(By.cssSelector("option:nth-child(3)"));
    frSelector.click();
    welcome = driver.findElement(By.cssSelector("p"));
    expected = "Bienvenu";
    actual = welcome.getText();
    assertEquals(expected, getWords(actual)[0]);
  }




/***
 * Test cases for Question 1(for the functional requirements)
  **/




//////Test for adding a book
/////for TC1 in functional requirements and Use case 3, TC3.3
 @Test
  public void adminAddsBook(){
  //  adminLogin();
    driver.get("http://localhost:8080/admin");
    WebElement loginid = driver.findElement(By.id("loginId"));
    loginid.sendKeys("admin");
    WebElement loginpass = driver.findElement(By.id("loginPasswd"));
    loginpass.sendKeys("password");
    WebElement loginButton = driver.findElement(By.id("loginBtn"));
    loginButton.click();
    //category
    WebElement category = driver.findElement(By.id("addBook-category"));
    category.sendKeys("fiction");
    //id
    WebElement bookId = driver.findElement(By.id("addBook-id"));
    bookId.sendKeys("123456");
    //title
    WebElement title = driver.findElement(By.id("addBook-title"));
    title.sendKeys("software quality assurance");
    //author
    WebElement bookauthor = driver.findElement(By.id("addBook-authors"));
    bookauthor.sendKeys("Daniel Galin");
    
    //cost
    WebElement cost = driver.findElement(By.id("cost"));
    cost.sendKeys("5");

    WebElement addButton = driver.findElement(By.name("addBook"));
    
    
    addButton.click();
   
   //now clicks on search button to make sure the book is added
     driver.findElement(By.id("searchBtn")).click();

    boolean bookAdded = false;

      if(driver.findElement(By.id("del-123456")) != null){
      bookAdded= true;}

   

    assertEquals(true,bookAdded);
   // assertEquals("", feedback);


  }


///for TC2 in question 1
  
  @Test
  public void addBookWrongId(){
    //  adminLogin();
    driver.get("http://localhost:8080/admin");
    WebElement loginid = driver.findElement(By.id("loginId"));
    loginid.sendKeys("admin");
    WebElement loginpass = driver.findElement(By.id("loginPasswd"));
    loginpass.sendKeys("password");
    WebElement loginButton = driver.findElement(By.id("loginBtn"));
    loginButton.click();
    //category
    WebElement category = driver.findElement(By.id("addBook-category"));
    category.sendKeys("fiction");
    //id
    WebElement bookId = driver.findElement(By.id("addBook-id"));
    bookId.sendKeys("123456000");
    //title
    WebElement title = driver.findElement(By.id("addBook-title"));
    title.sendKeys("software quality assurance");
    //author
    WebElement bookauthor = driver.findElement(By.id("addBook-authors"));
    bookauthor.sendKeys("Daniel Galin");
    
    //cost
    WebElement cost = driver.findElement(By.id("cost"));
    cost.sendKeys("5");

    WebElement addButton = driver.findElement(By.name("addBook"));

    //now goes to the catalog to look if the book was added
    driver.findElement(By.id("searchBtn")).click();

    boolean bookAdded = false;
    try{
      driver.findElement(By.id("del-123456000"));
      bookAdded= true;

    }
    catch(Exception e){
      bookAdded = false;

    }

    assertEquals(false,bookAdded);

}



///for TC13
@Test
  public void checkTax(){


     driver.get("http://localhost:8080");
      //first it adds an item so that the cart is not empty
    driver.findElement(By.id("searchBtn")).click(); 
    
    driver.findElement(By.id("order-hall001")).click();
    driver.findElement(By.id("cartLink")).click();
    
    String message = driver.findElement(By.className("content")).getText(); //used from test 2
    String totalCost = getWords(message)[17];
    int b = totalCost.length();
    //to have a string without the $ sign
    String a = totalCost.substring(1, (b));
    System.out.println(a);
    double cost = Double.parseDouble(a);
    
     System.out.println("cost is"+cost);
     double tax = (13) * (cost/100);
     System.out.println("tax is"+tax);
    cost =  cost + ((13/100)*cost);
    System.out.println("cost is"+cost);
    driver.findElement(By.name("checkout")).click();
    //String expected = "order_total";
    
    String tax1 = String.valueOf(tax);
    int d = tax1.length();
    String tax2 =tax1.substring(0,(d-2));

    String actual = driver.findElement(By.id("order_taxes")).getText();
    String expected = "$"+ (String.valueOf(tax2));
    assertEquals(expected,actual);
    //assertEquals(expected, getWords(message)[17]);
    //get the cost
  }


////for TC15
  @Test
  public void checkShippingFee(){
      driver.get("http://localhost:8080");
      //first it adds an item so that the cart is not empty
    driver.findElement(By.id("searchBtn")).click(); 
    
    driver.findElement(By.id("order-hall001")).click();
    driver.findElement(By.id("cartLink")).click();
    
    String message = driver.findElement(By.className("content")).getText(); //used from test 2
    String totalCost = getWords(message)[17];
    int b = totalCost.length();
    //to have a string without the $ sign
    String a = totalCost.substring(1, (b));
    System.out.println(a);
    double cost = Double.parseDouble(a);
    
     System.out.println("cost is"+cost);
     double shipping = (5) * (cost/100);
     shipping = shipping + 10;
     shipping = Math.round(shipping * 100);
shipping = shipping/100;
     //shipping = shiiping.round();
     //float saved = Math.round(shipping, 2);
      driver.findElement(By.name("checkout")).click();
      String sh = String.valueOf(shipping);
    int d = sh.length();
    String ship =sh.substring(0,(d-2));
     String actual = driver.findElement(By.id("order_shipping")).getText();
     String aa = totalCost.substring(1, (b));
     double actualll = Double.parseDouble(aa);
    String expected = "$"+ (String.valueOf(ship));

    //if the string doesn;t have the .00 part
    if(expected.indexOf('.')==-1){
      expected = expected +".00";
    }
    assertEquals(expected,actual);


  }



  //////////////////////
///////TESTS FOR USE CASES


//FOR TC1.1 IN QUESTION 2

 @Test
  public void loginCorrectCredintials(){
    //opens the app
    driver.get("http://localhost:8080/admin");
    //logins with admin username and password
    WebElement loginid = driver.findElement(By.id("loginId"));
    loginid.sendKeys("admin");
    WebElement loginpass = driver.findElement(By.id("loginPasswd"));
    loginpass.sendKeys("password");
     WebElement loginButton = driver.findElement(By.id("loginBtn"));
    loginButton.click();

    //succesfully signs in
    //then goes to /admin page and if it finds the sign out button
    //then the admin has logged in succesfully
    boolean flag = false;
    driver.get("http://localhost:8080/admin");
    try{
      driver.findElement(By.id("search-category-admin-form"));
      flag = true;

    }
    catch(Exception e){
      flag = false;
    }
    assertEquals(true,flag);

  }


//FOR TC1.2 IN QUESTION 2
  @Test
  public void loginIncorrectPass(){

    //opens the app
    driver.get("http://localhost:8080/admin");
    //logins with admin username and password
    WebElement loginid = driver.findElement(By.id("loginId"));
    loginid.sendKeys("admin");
    WebElement loginpass = driver.findElement(By.id("loginPasswd"));
    loginpass.sendKeys("passwor");
     WebElement loginButton = driver.findElement(By.id("loginBtn"));
    loginButton.click();
    String message = driver.findElement(By.className("content")).getText();

    String expected = "Invalid username and/or password\nUser Name\nPassword" ;
    if(expected==message){
  //  SoftAssert softAssert = new SoftAssert();
   // SoftAssert.assertEquals(expected,message);
     loginid.sendKeys("admin");
     loginpass.sendKeys("password");
     loginButton.click();

     boolean flag = false;
    driver.get("http://localhost:8080/admin");
    try{
      driver.findElement(By.id("search-category-admin-form"));
      flag = true;

    }
    catch(Exception e){
      flag = false;
    }
    assertEquals(true,flag);}
   // SoftAssert.assertEquals(true,flag);//}
    //SoftAssert.assertAll();

  }



  //Use case 2
  //FOR TC2.1 IN QUESTION 2
  @Test
  public void adminLogin(){
    //first it signs in as admin
     //opens the app
    driver.get("http://localhost:8080/admin");
    //logins with admin username and password
    WebElement loginid = driver.findElement(By.id("loginId"));
    loginid.sendKeys("admin");
    WebElement loginpass = driver.findElement(By.id("loginPasswd"));
    loginpass.sendKeys("password");
     WebElement loginButton = driver.findElement(By.id("loginBtn"));
    loginButton.click();

    //now clicks on sign out
    //Webelement signOutBttn = driver.findElement(By.cssSelector(input[value='Sign out']));
    // driver.findElement(By.value("Sign out"));
    WebElement signOut = driver.findElement(By.xpath("/html/body/div/div[2]/form[2]/input"));
    signOut.click();
   // signOutBttn.click();
    String message = driver.findElement(By.className("content")).getText();

    String expected = "You have been logged out\nUser Name\nPassword" ;
    assertEquals(expected,message);
  }


/// ///HERE


  ///////////////////////////
  ///// placeholder for use case 3



//FOR TC3.1 IN QUESTION 2

  @Test
  public void addInvalidBook(){
      driver.get("http://localhost:8080/admin");
    WebElement loginid = driver.findElement(By.id("loginId"));
    loginid.sendKeys("admin");
    WebElement loginpass = driver.findElement(By.id("loginPasswd"));
    loginpass.sendKeys("password");
    WebElement loginButton = driver.findElement(By.id("loginBtn"));
    loginButton.click();
    //category
    WebElement category = driver.findElement(By.id("addBook-category"));
    category.sendKeys("fiction");
    //id
    WebElement bookId = driver.findElement(By.id("addBook-id"));
    bookId.sendKeys("1234560000000");
    //title
    WebElement title = driver.findElement(By.id("addBook-title"));
    title.sendKeys("QA");
    //author
    WebElement bookauthor = driver.findElement(By.id("addBook-authors"));
    bookauthor.sendKeys("Galin");
    
    //cost
    WebElement cost = driver.findElement(By.id("cost"));
    cost.sendKeys("5");

    WebElement addButton = driver.findElement(By.name("addBook"));
    
    
    try{addButton.click();}
    catch(Exception e){
      //reads the error
      String message = driver.findElement(By.cssSelector("li")).getText(); //used from test 2
      //String expected = "Welcome";
      if(message.equals("The Book Id must be between 5 and 8 character long")){
       // int i = Randomizer.generate(11111,11111111);
        //String s = String.valueOf(i);
         bookId.sendKeys("9672683");

      }
     
    }

    addButton.click();
     boolean bookAdded = false;
    try{
      if(driver.findElement(By.id("del-123456000"))!=null);
      bookAdded= true;

    }
    catch(Exception e){
      bookAdded = false;

    }

    assertEquals(false,bookAdded);


  }
  

//FOR TC3.2 IN QUESTION 2

  @Test
  public void addBookRepeatedId(){
      driver.get("http://localhost:8080/admin");
    WebElement loginid = driver.findElement(By.id("loginId"));
    loginid.sendKeys("admin");
    WebElement loginpass = driver.findElement(By.id("loginPasswd"));
    loginpass.sendKeys("password");
    WebElement loginButton = driver.findElement(By.id("loginBtn"));
    loginButton.click();
    //category
    WebElement category = driver.findElement(By.id("addBook-category"));
    category.sendKeys("fiction");
    //id
    WebElement bookId = driver.findElement(By.id("addBook-id"));
    bookId.sendKeys("hall001");
    //title
    WebElement title = driver.findElement(By.id("addBook-title"));
    title.sendKeys("QA");
    //author
    WebElement bookauthor = driver.findElement(By.id("addBook-authors"));
    bookauthor.sendKeys("Galin");
    
    //cost
    WebElement cost = driver.findElement(By.id("cost"));
    cost.sendKeys("5");

    WebElement addButton = driver.findElement(By.name("addBook"));
    
    
    try{addButton.click();}
    catch(Exception e){
      //reads the error
      String message = driver.findElement(By.id("feedback")).getText(); //used from test 2
      //String expected = "Welcome";
      if(message.equals("Book with same id already exist")){
       // int i = Randomizer.generate(11111,11111111);
        //String s = String.valueOf(i);
         bookId.sendKeys("9672683");

      }
     
    }

    addButton.click();
     boolean bookAdded = false;
    try{
      if(driver.findElement(By.id("del-123456000"))!=null);
      bookAdded= true;

    }
    catch(Exception e){
      bookAdded = false;

    }

    assertEquals(false,bookAdded);


  }




  ////Use case 4

//FOR TC4.1 IN QUESTION 2
   @Test
    public void nocategory(){
      driver.get("http://localhost:8080");
      driver.findElement(By.id("searchBtn")).click();
      String message = driver.findElement(By.className("content")).getText();
      boolean flag = false;
      //checks if the message that "no matching category isn't there, which means all the books show up"
      if(message!="Sorry we do not have any item matching category '' at this moment"){
        flag = true;
      }
      //String expected = "We currently have the following items in category  ''";
      assertEquals(true,flag);

    }

 //FOR TC4.2 IN QUESTION 2
  @Test
    public void categoryDoesntExist(){
      driver.get("http://localhost:8080");
      WebElement b = driver.findElement(By.id("search"));
      b.sendKeys("scientific");
      driver.findElement(By.id("searchBtn")).click();
      
       String message = driver.findElement(By.className("content")).getText();
       String expected = "Sorry we do not have any item matching category 'scientific' at this moment";
       assertEquals(expected,message);
    }



  //for this test I had to add a book manually as admin
    //user searches for a book category and finds it
    //FOR TC4.3 IN QUESTION 2

    @Test
    public void categoryExists(){
         ///first I have to add the book manually
       driver.get("http://localhost:8080/admin");
    WebElement loginid = driver.findElement(By.id("loginId"));
    loginid.sendKeys("admin");
    WebElement loginpass = driver.findElement(By.id("loginPasswd"));
    loginpass.sendKeys("password");
    WebElement loginButton = driver.findElement(By.id("loginBtn"));
    loginButton.click();
    //category
    WebElement category = driver.findElement(By.id("addBook-category"));
    category.sendKeys("engineering");
    //id
    WebElement bookId = driver.findElement(By.id("addBook-id"));
    bookId.sendKeys("987654000");
    //title
    WebElement title = driver.findElement(By.id("addBook-title"));
    title.sendKeys("quality assurance");
    //author
    WebElement bookauthor = driver.findElement(By.id("addBook-authors"));
    bookauthor.sendKeys("John");
    
    //cost
    WebElement cost = driver.findElement(By.id("cost"));
    cost.sendKeys("50");
    driver.findElement(By.name("addBook")).click();
      WebElement b = driver.findElement(By.id("search"));
      b.sendKeys("engineering");
      driver.findElement(By.id("searchBtn")).click();
       String message = driver.findElement(By.cssSelector("h1")).getText(); //By.cssSelector("p"));
       String expected = "We currently have the following items in category 'engineering'";
       assertEquals(expected,message);

    }
    


    ///for Use case 5
    //FOR T5.1 IN QUESTION 2
    @Test
    public void removesBook(){
      ///first I have to add the book manually
       driver.get("http://localhost:8080/admin");
    WebElement loginid = driver.findElement(By.id("loginId"));
    loginid.sendKeys("admin");
    WebElement loginpass = driver.findElement(By.id("loginPasswd"));
    loginpass.sendKeys("password");
    WebElement loginButton = driver.findElement(By.id("loginBtn"));
    loginButton.click();
    //category
    WebElement category = driver.findElement(By.id("addBook-category"));
    category.sendKeys("engineering");
    //id
    WebElement bookId = driver.findElement(By.id("addBook-id"));
    bookId.sendKeys("9876541");
    //title
    WebElement title = driver.findElement(By.id("addBook-title"));
    title.sendKeys("software quality assurance");
    //author
    WebElement bookauthor = driver.findElement(By.id("addBook-authors"));
    bookauthor.sendKeys("George");
    
    //cost
    WebElement cost = driver.findElement(By.id("cost"));
    cost.sendKeys("5");
    driver.findElement(By.name("addBook")).click();
   //  driver.get("http://localhost:8080/admin");
    //logins with admin username and password
   // WebElement loginid = driver.findElement(By.id("loginId"));
   // loginid.sendKeys("admin");
    //WebElement loginpass = driver.findElement(By.id("loginPasswd"));
    //loginpass.sendKeys("password");
    // WebElement loginButton = driver.findElement(By.id("loginBtn"));
 //   loginButton.click();
    driver.findElement(By.id("searchBtn")).click();
    driver.findElement(By.id("del-9876541")).click();
    boolean flag = false;
   // try{
      if(driver.findElement(By.id("del-9876541"))==null){
      flag = true;}
    //}
    //catch(Exception e){
    //  flag = false;

   // }

   // boolean found = driver.findElement(By.id("searchBtn"));
    assertEquals(false,flag);


    }


     //for use case 6
    //FOR T6.1 IN QUESTION 2

    @Test
    public void orderNewBook(){
       driver.get("http://localhost:8080");
    
    driver.findElement(By.id("searchBtn")).click(); 
    String savedCost = driver.findElement(By.id("cost-hall001")).getText();
    driver.findElement(By.id("order-hall001")).click();
    driver.findElement(By.id("cartLink")).click();
    String cost = driver.findElement(By.id("tothall001")).getText();

    boolean costMoreThan = false;
    //this makes sure that the cost of the book is added so the book is added to the cart
   // if(Integer.valueOf(cost)>=Integer.valueOf(savedCost)){

    if((cost!="$0.00" ) || (cost ==savedCost)){
      costMoreThan = true;
    }

    
    assertEquals(true, costMoreThan);

    }


    //FOR T6.2 IN QUESTION 2
    @Test
    public void changesNumOfOrder(){
       driver.get("http://localhost:8080");
    
    driver.findElement(By.id("searchBtn")).click(); 
    String savedCost = driver.findElement(By.id("cost-hall001")).getText();
    driver.findElement(By.id("order-hall001")).click();
    driver.findElement(By.id("order-hall001")).click();
    driver.findElement(By.id("cartLink")).click();
  //  driver.findElement(By.id("cartLink")).click(); //adds it the 2nd time
    String numOfBooks = driver.findElement(By.id("hall001")).getText();
    //hall001


    boolean numMoreThan = false;
    

    if(numOfBooks!="1" ){
      numMoreThan = true;
    }

    
    assertEquals(true, numMoreThan);

    }


    ///FOR T7.1 IN QUESTION 2


    @Test
    public void viewOrder(){

    driver.get("http://localhost:8080");
      //first it adds an item so that the cart is not empty
    driver.findElement(By.id("searchBtn")).click(); 
    
    driver.findElement(By.id("order-hall001")).click();
    driver.findElement(By.id("cartLink")).click();
    
    String message = driver.findElement(By.className("content")).getText(); //used from test 2
    String expected = "hall001";
    assertEquals(expected, getWords(message)[7]);
    }



   // /use case 8

    //changes the number of items from 1 to 8 and clicks on update
    //then cheks to make sure the number is updated

    ////FOR T8.1 IN QUESTION 2
    
 @Test
    public void updateNumBooks(){
       driver.get("http://localhost:8080");
    
    //first it adds an item so that the cart is not empty
    driver.findElement(By.id("searchBtn")).click(); 
    
    driver.findElement(By.id("order-hall001")).click();
    driver.findElement(By.id("cartLink")).click();
    WebElement a = driver.findElement(By.id("hall001"));
    a.clear();
    a.sendKeys("8");
    driver.findElement(By.cssSelector("[name='updateOrder']")).click();
   
       String b = driver.findElement(By.id("hall001")).getText();

    //String saved = driver.findElement(By.id("hall001")).getText();
    String saved = a.getText();
    boolean flag = false;
    String expected = "8";
    //if()
    //.sendKeys
    //hall001
    assertEquals("",b);

  }


//////FOR T8.2 IN QUESTION 2
  @Test
    public void decreasingNumBooks(){
       driver.get("http://localhost:8080");
    
    //first it adds an item so that the cart is not empty
    driver.findElement(By.id("searchBtn")).click(); 
    
    driver.findElement(By.id("order-hall001")).click();
    driver.findElement(By.id("cartLink")).click();
    WebElement a = driver.findElement(By.id("hall001"));
    a.clear();
    a.sendKeys("0");
    driver.findElement(By.name("updateOrder")).click();

    String saved = driver.findElement(By.id("hall001")).getText();
    boolean flag = false;
    String expected = "8";
    //if()
    //.sendKeys
    //hall001
    assertEquals("",saved);

  }




///Use case 9
//////FOR T9.1 IN QUESTION 2

  @Test
    public void checkoutBook(){

  //first it will add 1 book to the cart

    driver.get("http://localhost:8080");
    
    driver.findElement(By.id("searchBtn")).click(); 
    String savedCost = driver.findElement(By.id("cost-hall001")).getText();
    driver.findElement(By.id("order-hall001")).click();
    driver.findElement(By.id("cartLink")).click();

    //clicks on checkout
    driver.findElement(By.name("checkout")).click();
//so when we see the order date it means the order was recieved and we have checked out our order
    String orderDate = driver.findElement(By.id("order_date")).getText();

    boolean flag = false;
    if(orderDate!=null){
      flag = true;
    }

    assertEquals(true,flag);
    }



    //use case 10

    //////FOR T10.1 IN QUESTION 2

  //I have used some part of the given test2 in this assignment
    @Test
    public void selectLanguage(){
        driver.get("http://localhost:8080");
      //langSelector.click(); 
    WebElement langSelector = driver.findElement(By.cssSelector("option:nth-child(2)"));   //used from test2 given
    langSelector.click();
    String message = driver.findElement(By.cssSelector("p")).getText(); //used from test 2
    String expected = "Welcome";
    assertEquals(expected, getWords(message)[0]);


    }

 private String[] getWords(String s) {
    
    return s.split("\\s+");
  }

}