import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.netology.Comment
import ru.netology.Note
import ru.netology.NoteService

class NoteServiceTest {
    @Before
    fun resetNote() {
        NoteService.resetNote()
    }

    @Test
    fun addCheck() {
        var note1 = Note(title = "Заголовок заметки №1", text = "Содержание заметки #1")
        note1 = NoteService.add(note1)
        note1 = NoteService.add(note1)
        assertEquals(1, note1.id)
    }

    @Test
    fun createCommentCheckTrue() {
        var note1 = Note(title = "Заголовок заметки №1", text = "Содержание заметки #1")
        var commentForNote1 = Comment(message = "Ахахахаха, верю!")
        note1 = NoteService.add(note1)
        commentForNote1 = NoteService.createComment(idNote = note1.id, commentForNote1)

        assertEquals(0, commentForNote1.id)
    }

    @Test
    fun createCommentCheckFalse() {
        var note1 = Note(title = "Заголовок заметки №1", text = "Содержание заметки #1")
        var commentForNote1 = Comment(message = "Ахахахаха, верю!")
        note1 = NoteService.add(note1)
        commentForNote1 = NoteService.createComment(idNote = note1.id, commentForNote1)

        assertEquals(0, commentForNote1.id)
    }

    @Test(expected = RuntimeException::class)
    fun createCommentCheckException() {
        var note1 = Note(title = "Заголовок заметки №1", text = "Содержание заметки #1")
        var commentForNote1 = Comment(message = "Ахахахаха, верю!")
        note1 = NoteService.add(note1)
        commentForNote1 = NoteService.createComment(idNote = note1.id - 1, commentForNote1)
    }

    @Test
    fun deleteNote() {
        var note1 = Note(title = "Заголовок заметки №1", text = "Содержание заметки #1")
        note1 = NoteService.add(note1)
        note1 = NoteService.delete(note1.id)
        assertEquals(false, note1.show)
    }

    @Test(expected = RuntimeException::class)
    fun deleteNoteExeption() {
        var note1 = Note(title = "Заголовок заметки №1", text = "Содержание заметки #1")
        note1 = NoteService.add(note1)
        note1 = NoteService.delete(note1.id - 100)
    }

    @Test
    fun deleteCommentTrue() {
        var note1 = Note(title = "Заголовок заметки №1", text = "Содержание заметки #1")
        var commentForNote1 = Comment(message = "Ахахахаха, верю!")
        note1 = NoteService.add(note1)
        commentForNote1 = NoteService.createComment(idNote = note1.id, commentForNote1)
        assertEquals(true, NoteService.deleteComment(note1.id, commentForNote1.id))
    }

    @Test(expected = RuntimeException::class)
    fun deleteCommentTrueExeption() {
        var note1 = Note(title = "Заголовок заметки №1", text = "Содержание заметки #1")
        var commentForNote1 = Comment(message = "Ахахахаха, верю!")
        note1 = NoteService.add(note1)
        commentForNote1 = NoteService.createComment(idNote = note1.id, commentForNote1)
        NoteService.deleteComment(note1.id, commentForNote1.id + 100)
    }

    @Test
    fun editTrue() {
        var note1 = Note(title = "Заголовок заметки №1", text = "Содержание заметки #1")
        note1 = NoteService.add(note1)
        note1 = note1.copy(title = "Новый титул")
        note1 = NoteService.edit(note1.id, note1)
        assertEquals("Новый титул", note1.title)
    }

    @Test
    fun editComment() {
        var note1 = Note(title = "Заголовок заметки №1", text = "Содержание заметки #1")
        var commentForNote1 = Comment(message = "Ахахахаха, верю!")
        note1 = NoteService.add(note1)
        commentForNote1 = NoteService.createComment(idNote = note1.id, commentForNote1)
        commentForNote1 = commentForNote1.copy(message = "Hello... It`s me....")
        commentForNote1 = NoteService.editComment(
            note1.id,
            commentForNote1.id,
            commentForNote1
        )
        assertEquals("Hello... It`s me....", commentForNote1.message)
    }

}