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
