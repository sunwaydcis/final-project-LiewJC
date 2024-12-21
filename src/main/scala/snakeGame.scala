import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage

class Point(var x: Int, var y: Int)

class SnakeGame extends Application:
  val Width = 600
  val Height = 600
  val CellSize = 20
  val Cols = Width / CellSize
  val Rows = Height / CellSize

  var snake = ArrayBuffer[Point](new Point(5, 5))
  var direction = new Point(1, 0)
  var food = generateFood()
  var gameOver = false
  var score = 0
  var frameCounter = 0
  val UpdateInterval = 8

  def generateFood(): Point =
    var foodPoint = new Point(Random.nextInt(Cols), Random.nextInt(Rows))
    while snake.exists(p => p.x == foodPoint.x && p.y == foodPoint.y) do
      foodPoint = new Point(Random.nextInt(Cols), Random.nextInt(Rows))
    foodPoint

  override def start(primaryStage: Stage): Unit =
    primaryStage.setTitle("Snake Game - Score: " + score.toString)
    val root = new StackPane()
    val canvas = new Canvas(Width, Height)
    val gc = canvas.getGraphicsContext2D()
    root.getChildren().add(canvas)

    val scene = new Scene(root)
    primaryStage.setScene(scene)

    scene.setOnKeyPressed(e =>
      e.getCode match
        case KeyCode.UP if direction.y != 1 => direction = new Point(0, -1)
        case KeyCode.DOWN if direction.y != -1 => direction = new Point(0, 1)
        case KeyCode.LEFT if direction.x != 1 => direction = new Point(-1, 0)
        case KeyCode.RIGHT if direction.x != -1 => direction = new Point(1, 0)
        case _ => ()
    )

    scene.setOnKeyReleased(e =>
      e.getCode match
        case KeyCode.SPACE if gameOver => restartGame(); gameOver = false
        case _ => ()
    )

    val timer = new AnimationTimer {
      override def handle(now: Long): Unit =
        if !gameOver then
          frameCounter += 1
          if frameCounter >= UpdateInterval then
            update()
            frameCounter = 0
          draw(gc)
          primaryStage.setTitle("Snake Game - Score: " + score.toString)
    }
    timer.start()
    primaryStage.show()

    def update(): Unit =
    val head = snake.head
    val newHead = new Point(
      (head.x + direction.x + Cols) % Cols,
      (head.y + direction.y + Rows) % Rows
    )

    if snake.exists(p => p.x == newHead.x && p.y == newHead.y) then
      gameOver = true
    else
      snake.prepend(newHead)
      if newHead.x == food.x && newHead.y == food.y then
        food = generateFood()
        score += 1
      else
        snake.remove(snake.length - 1)

    def draw(gc: javafx.scene.canvas.GraphicsContext): Unit =
      // Clear background
      gc.setFill(Color.BLACK)
      gc.fillRect(0, 0, Width, Height)
    
      // Draw snake
      gc.setFill(Color.GREEN)
      snake.foreach(p =>
        gc.fillRect(p.x * CellSize, p.y * CellSize, CellSize - 1, CellSize - 1)
      )
  
      // Draw food
      gc.setFill(Color.RED)
      gc.fillRect(food.x * CellSize, food.y * CellSize, CellSize - 1, CellSize - 1)
  
      // Draw game over
      if gameOver then
        gc.setFill(Color.WHITE)
        gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER)
        gc.setFont(javafx.scene.text.Font.font(30))
        gc.fillText("Game Over! Score: " + score.toString, Width/2, Height/2)
        gc.fillText("Press Space to restart", Width/2, Height/2 + 40)
