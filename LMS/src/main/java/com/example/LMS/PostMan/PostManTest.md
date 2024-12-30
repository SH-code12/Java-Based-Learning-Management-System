# Instractor

## Function Name: CreateCourse

## Path: 
```
http://localhost:8080/instructor/createCourse
```

## Method: **`Post`**

## Body

```bash
{
    "courseId": "CS352",
    "title": "Advanced Software Engineer",
    "description": "Learn the Design and Architecture Patterns .",
    "durationHours": 3,
    "listLessons": [],
    "mediaFiles": []
}

```

## Function Name: updateCourse

## Path: 
```
http://localhost:8080/instructor/{courseId}/update
```
## Method: **`Put`**

## Body

```bash
{
    "courseId": "CS Update",
    "title": "Intro to CS - Updated",
    "description": "Updated course description.",
    "durationHours": 4,
    "listLessons": [],
    "mediaFiles": []
}

```

## Function Name: uploadMedia

## Path: 
```
http://localhost:8080/instructor/{courseId}/upload-media
```

## Method: **`Post`**

## Body

**`form-data`** , type: **`file`**

## Function Name: getEnrolledStudents

## Path: 
```
http://localhost:8080/instructor/{courseId}/students
```

## Method: **`Get`**

## Function Name: createQuiz

## Path:
```
http://localhost:8080/instructor/createQuiz
```

## Method: **`Post`**

## Body

```bash

{
  "quizTitle": "Sample Quiz",
  "course":{
    "id": 1
  },
  "questions": []
}

```

## Function Name: getRandomQuestions

## Path: 
```
http://localhost:8080/instructor/{quizId}/randomQuestions
```

## Method: **`Get`**

## Function Name: sendNotificationByEmail

## Path: 
```
http://localhost:8080/instructor/sendByEmail
```

## Method: **`Post`**

## Body

```bash

{
  "userId": 1,
  "type": "Alert",
  "message": "Your subscription is about to expire.",
  "timestamp": "2024-12-31T12:00:00"
}

```

## Function Name: addQuestion

## Path: 
```
http://localhost:8080/instructor/{quizId}/addQuestion

```

## Method: **`Post`**

## Body

```bash

{
  "questionText": "What is 2 + 2?",
  "type": "MCQ",
  "options": ["1", "2", "3", "4"],
  "correctAnswer": "4"
}

```

# Student

## Function Name: displayCourses

## Path: 
```
http://localhost:8080/student/displayCourses
```

## Method: **`Get`**

## Function Name: getCourseMaterials

## Path: 
```
http://localhost:8080/student/{courseId}/materials
```

## Method: **`Get`**
