package strikt

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals
import strikt.api.expect
import strikt.assertions.*
import java.time.LocalDate

internal object Mapping : Spek({
  describe("standard library mappings") {
    describe("mapping assertions on ${Iterable::class.java.simpleName}") {
      it("maps to the first element using first") {
        val subject = listOf("catflap", "rubberplant", "marzipan")
        expect(subject).first().isEqualTo("catflap")
      }

      it("maps to the last element using first") {
        val subject = listOf("catflap", "rubberplant", "marzipan")
        expect(subject).last().isEqualTo("marzipan")
      }
    }

    describe("mapping assertions on ${List::class.java.simpleName}") {
      it("maps to an indexed element using [int]") {
        val subject = listOf("catflap", "rubberplant", "marzipan")
        expect(subject)[1].isEqualTo("rubberplant")
      }

      it("maps to a ranged sub-list using [IntRange]") {
        val subject = listOf("catflap", "rubberplant", "marzipan")
        expect(subject)[1..2].containsExactly("rubberplant", "marzipan")
      }
    }
  }

  describe("custom mapping") {
    data class Person(val name: String, val birthDate: LocalDate)

    val subject = Person("David", LocalDate.of(1947, 1, 8))

    it("maps the assertion subject to the closure result") {
      expect(subject) {
        map { name }.isEqualTo("David")
        map { birthDate }.map { year }.isEqualTo(1947)
      }
    }

    it("can use property syntax") {
      expect(subject) {
        map(Person::name).isEqualTo("David")
        map(Person::birthDate).map(LocalDate::getYear).isEqualTo(1947)
      }
    }

    it("allows methods to be called") {
      expect(subject) {
        map { name.toUpperCase() }.isEqualTo("DAVID")
        map { birthDate.plusYears(69).plusDays(2) }.isEqualTo(LocalDate.of(2016, 1, 10))
      }
    }

    it("automatically maps a Kotlin property name to the downstream subject description") {
      fails {
        expect(subject).map(Person::name).isEqualTo("Ziggy")
      }.let { e ->
        val expectedMessage = listOf(
          "▼ Expect that Person(name=David, birthDate=1947-01-08)",
          "  ▼ .name \"${subject.name}\"",
          "    ✗ is equal to Ziggy",
          ""
        )
        assertEquals(expectedMessage, e.message.lines())
      }
    }

    it("automatically maps a Java property name to the downstream subject description") {
      fails {
        expect(subject).map(Person::birthDate).map(LocalDate::getYear).isEqualTo(1971)
      }.let { e ->
        val expectedMessage = listOf(
          "▼ Expect that Person(name=David, birthDate=1947-01-08)",
          "  ▼ .birthDate ${subject.birthDate}",
          "    ▼ .year ${subject.birthDate.year}",
          "      ✗ is equal to 1971",
          ""
        )
        assertEquals(expectedMessage, e.message.lines())
      }
    }
  }
})
