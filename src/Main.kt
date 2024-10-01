import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlin.system.measureTimeMillis


suspend fun main() = coroutineScope {

    val personsList: MutableList<String> = mutableListOf()
    val personsPhones: MutableList<String> = mutableListOf()
    val personInfo: MutableList<String> = mutableListOf()

    val persons = arrayOf(
        Person("Виктор", "Бухгалтер"),
        Person("Елена", "Директор"),
        Person("Полина", "Менеджер"),
        Person("Александр", "Маркетолог"),
    )

    val time = measureTimeMillis {
        withContext(newSingleThreadContext("persons_thread_context")){
            launch {
                getPersonsFlow(persons).collect { person ->
                    personsList.add(person) }
            }
            launch {
                getPhoneFlow(persons.size).collect{ phone -> personsPhones.add(phone) }
            }
        }
    }
    println("Общее затраченное время $time")
    println(personsList.zip(personsPhones))

}




fun getPersonsFlow(persons: Array<Person>) = flow{
    for (item in persons){
        delay(500L)
        emit(item.toString())
    }
}

fun getPhoneFlow(size:Int) = flow{
    val start = "+7917"
    repeat(size){
        val number = start + (0..9999999).random().toString()
        delay(500L)
        emit(number)
    }
}

