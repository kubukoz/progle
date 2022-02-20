// using scala 3.1.1
// using lib org.typelevel::cats-effect:3.3.5
import cats.effect.IOApp

import cats.implicits._
import cats.effect.IO
import cats.effect.std.Random

object Main extends IOApp.Simple {

  case class Language(name: String, snippet: String)

  val languages: List[Language] = List(
    Language("scala", """def main() = println("Hello, world!")"""),
    Language("rust", """fn main() { println!("hello world!") }"""),
    Language("elm", """Main.main = (IO.succeed "hello world!")"""),
    Language("haskell", """main = putStrLn "hello world!""""),
    Language("python", """print "hello world!""""),
    Language("javascript", """console.log("hello world!")""")
  )

  val run = Random
    .scalaUtilRandom[IO]
    .flatMap(_.nextIntBounded(languages.size).map(languages(_)))
    .flatMap { language =>
      def takeGuesses(remaining: Int): IO[Unit] = if (remaining == 0) IO.raiseError(new Throwable("Failed game"))
      else
        (
          IO.print(s"${Console.GREEN}> ") *>
            IO.readLine.map(_.some).handleErrorWith(_ => IO.print(Console.RESET).as(none)) <*
            IO.print(Console.RESET)
        )
          .map(_.map(_.trim))
          .flatMap {
            case Some(guess) if language.name.equalsIgnoreCase(guess) => IO.println("Congratulations, you guessed correctly!")

            case Some(s) if s.equalsIgnoreCase("java script") =>
              IO.println("dude what the fuck") *> takeGuesses(remaining)

            case Some(notALanguge) if !languages.exists(_.name.equalsIgnoreCase(notALanguge)) =>
              IO.println(s"$notALanguge is not a known language.") *> takeGuesses(remaining)

            case Some(_) =>
              IO.println(s"Incorrect! ${remaining - 1} guesses remaining.") *>
                takeGuesses(remaining - 1)
            case None    =>
              IO.unit
          }

      IO.println(language.snippet) *>
        IO.println("Guess the language!") *>
        takeGuesses(6)
    }

}
