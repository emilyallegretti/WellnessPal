package com.bignerdranch.android.wellnesspal

import com.bignerdranch.android.wellnesspal.models.Goal
import com.bignerdranch.android.wellnesspal.models.Pet
import com.bignerdranch.android.wellnesspal.models.User
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun create_goal() {
        val num1 = "1"
        val num2 = "2"
        val num3 = "3"

        //Test goal constructor stores values correctly
        val goal = Goal(waterGoal = num1, eatGoal = num2, sleepGoal = num3)
        assertEquals(goal.waterGoal, num1)
        assertEquals(goal.eatGoal, num2)
        assertEquals(goal.sleepGoal, num3)
    }

    @Test
    fun copy_goal(){
        val water1 = "1"
        val eat1 = "3"
        val sleep1 = "5"

        var goal1 = Goal(waterGoal = water1, eatGoal = eat1, sleepGoal = sleep1)
        var goal2 = goal1
        assertEquals(goal1.waterGoal, water1)
        assertEquals(goal1.sleepGoal, sleep1)
        assertEquals(goal1.eatGoal, eat1)

        assertEquals(goal2.waterGoal, water1)
        assertEquals(goal2.sleepGoal, sleep1)
        assertEquals(goal2.eatGoal, eat1)
    }

    @Test
    fun read_goal(){
        val goal = Goal(waterGoal = "1", eatGoal = "2", sleepGoal = "3")

        val water = goal.waterGoal
        val eat = goal.eatGoal
        val sleep = goal.sleepGoal

        assertEquals(water, goal.waterGoal)
        assertEquals(eat, goal.eatGoal)
        assertEquals(sleep, goal.sleepGoal)
    }

    @Test
    fun create_pet(){
        val name = "name"
        val age = 1
        val color = "blue"
        val emotion = "happy"
        val current = true
        val birthdayOne = false
        val birthdayTwo = false
        val birthdayThree = false

        //check that per constructor stores values correctly
        val pet = Pet(name = name, age = age, color = color, emotion = emotion, current = current, birthdayOne = birthdayOne, birthdayTwo = birthdayTwo, birthdayThree = birthdayThree)
        assertEquals(pet.name, name)
        assertEquals(pet.age, age)
        assertEquals(pet.color, color)
        assertEquals(pet.emotion, emotion)
        assertEquals(pet.current, current)
        assertEquals(pet.birthdayOne, birthdayOne)
        assertEquals(pet.birthdayTwo, birthdayTwo)
        assertEquals(pet.birthdayThree, birthdayThree)
    }

    @Test
    fun copy_pet(){
        val name = "name"
        val age = 1
        val color = "blue"
        val emotion = "happy"
        val current = true
        val birthdayOne = false
        val birthdayTwo = false
        val birthdayThree = false

        var pet1 = Pet(name = name, age = age, color = color, emotion = emotion, current = current, birthdayOne = birthdayOne, birthdayTwo = birthdayTwo, birthdayThree = birthdayThree)
        var pet2 = pet1
        assertEquals(pet1.name, name)
        assertEquals(pet1.age, age)
        assertEquals(pet1.color, color)
        assertEquals(pet1.emotion, emotion)
        assertEquals(pet1.current, current)
        assertEquals(pet1.birthdayOne, birthdayOne)
        assertEquals(pet1.birthdayTwo, birthdayTwo)
        assertEquals(pet1.birthdayThree, birthdayThree)

        assertEquals(pet2.name, name)
        assertEquals(pet2.age, age)
        assertEquals(pet2.color, color)
        assertEquals(pet2.emotion, emotion)
        assertEquals(pet2.current, current)
        assertEquals(pet2.birthdayOne, birthdayOne)
        assertEquals(pet2.birthdayTwo, birthdayTwo)
        assertEquals(pet2.birthdayThree, birthdayThree)
    }

    @Test
    fun read_pet(){
        val pet = Pet(name = "Emma", age = 2, color = "blue", emotion = "sad", current = false, birthdayOne = true, birthdayTwo = true, birthdayThree = true)
        val name = pet.name
        val age = pet.age
        val color = pet.color
        val emotion = pet.emotion
        val current = pet.current
        val birthday1 = pet.birthdayOne
        val birthday2 = pet.birthdayTwo
        val birthday3 = pet.birthdayThree

        assertEquals(name, pet.name)
        assertEquals(age, pet.age)
        assertEquals(color, pet.color)
        assertEquals(emotion, pet.emotion)
        assertEquals(current, pet.current)
        assertEquals(birthday1, pet.birthdayOne)
        assertEquals(birthday2, pet.birthdayTwo)
        assertEquals(birthday3, pet.birthdayThree)
    }

    @Test
    fun create_user(){
        val email = "emma@emma.com"
        val fname = "emma"
        val lname = "ryan"

        //check that user constructor stores values correctly
        val user = User(email = email, fname = fname, lname = lname)
        assertEquals(user.email, email)
        assertEquals(user.fname, fname)
        assertEquals(user.lname, lname)
    }

    @Test
    fun copy_user(){
        val email = "emma@emma.com"
        val fname = "emma"
        val lname = "ryan"

        var user1 = User(email = email, fname = fname, lname = lname)
        var user2 = user1
        assertEquals(user1.email, email)
        assertEquals(user1.fname, fname)
        assertEquals(user1.lname, lname)

        assertEquals(user2.email, email)
        assertEquals(user2.fname, fname)
        assertEquals(user2.lname, lname)

    }

    @Test
    fun read_user(){
        val user = User(email = "emma@emma.com", fname = "Emma", lname = "Ryan")
        val email = user.email
        val fname = user.fname
        val lname = user.lname

        assertEquals(email, user.email)
        assertEquals(fname, user.fname)
        assertEquals(lname, user.lname)
    }


}