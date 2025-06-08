package ru.netology

import java.lang.reflect.Array
import kotlin.collections.mutableListOf

data class Comment(
    val id: Int = -1,
    var message: String,
    val show: Boolean = true
)

data class NoteWithIndex(
    val index: Int,
    val note: Note
)

data class Note(
    var id: Int = -1,
    var title: String,
    val text: String,
    val privacy: Int = 1,
    val commentPrivacy: Int = 1,
    val privacyView: String = "null",
    val privacyComment: String = "null",
    var show: Boolean = true,
    var comments: MutableList<Comment> = mutableListOf()
)

class NoteNotFoundException(message: String) : RuntimeException(message)
class CommentNotFoundException(message: String) : RuntimeException(message)

object NoteService {
    private var notes = mutableListOf<Note>()
    private var comments = mutableListOf<Comment>()

    fun add(note: Note): Note {
        val newNote = note.copy(id = if (!notes.isEmpty()) notes.last().id + 1 else 0)
        notes.add(newNote)
        return newNote
    }

    fun createComment(idNote: Int, comment: Comment): Comment {
        val noteWithIndex = getNoteByIdWithIndex(idNote)
        val newComment = comment.copy(
            id = if (!noteWithIndex.note.comments.isEmpty())
                noteWithIndex.note.comments.last().id + 1
            else
                0
        )
        comments = noteWithIndex.note.comments
        comments.add(newComment)
        notes[noteWithIndex.index] = notes[noteWithIndex.index].copy(comments = comments)
        return newComment
    }

    fun delete(idNote: Int): Note {
        val noteWithIndex = getNoteByIdWithIndex(idNote)
        notes[noteWithIndex.index] = notes[noteWithIndex.index].copy(show = false)
        return notes[noteWithIndex.index]
    }

    fun deleteComment(idNote: Int, idComment: Int): Boolean {
        val noteWithIndex = getNoteByIdWithIndex(idNote)
        for ((index, comment) in noteWithIndex.note.comments.withIndex()) {
            if (comment.id == idComment) {
                notes[noteWithIndex.index].comments[index] =
                    notes[noteWithIndex.index].comments[index].copy(show = false)
                return true
            }
        }
        throw CommentNotFoundException("Комментарий к посту $idNote, с id = $idComment не найден")
    }

    fun edit(idNote: Int, editNote: Note): Note {
        val noteWithIndex = getNoteByIdWithIndex(idNote)
        val newNote = editNote.copy(id = idNote)
        notes[noteWithIndex.index] = newNote
        return newNote
    }

    fun editComment(idNote: Int, idComment: Int, editComment: Comment): Comment {
        var noteWithIndex = getNoteByIdWithIndex(idNote)
        comments = noteWithIndex.note.comments
        for ((index, comment) in comments.withIndex()) {
            if (comment.id == idComment) {
                comments[index] = editComment.copy(id = idComment)
                notes[noteWithIndex.index].comments = comments
                return comments[index]
            }
        }
        throw CommentNotFoundException("Комментарий к посту $idNote, с id = $idComment не найден")
    }

    fun get(): MutableList<Note> {
        return notes
    }

    fun getById(idNote: Int): Note {
        for (note in notes) {
            if (note.id == idNote) return note
        }
        throw NoteNotFoundException("Заметка с индексом $idNote не найдена")
    }


    fun getComments(idNote: Int): MutableList<Comment> {
        return getById(idNote).comments
    }

    fun restoreComment(idNote: Int, idComment: Int): Boolean {
        val noteWithIndex = getNoteByIdWithIndex(idNote)
        for ((index, comment) in noteWithIndex.note.comments.withIndex()) {
            if (comment.id == idComment) {
                notes[noteWithIndex.index].comments[index] =
                    notes[noteWithIndex.index].comments[index].copy(show = true)
                return true
            }
        }
        throw CommentNotFoundException("Комментарий к посту $idNote, с id = $idComment не найден")
    }


    override fun toString(): String {
        var data: String = "list of notes:\n[\n"
        for (note in notes) {
            data += "\t[" + note.id + "," + note.title + ",show=" + note.show + ", comments:"
            for (comment in note.comments)
                data += "\n\t\t$comment"
            data += "\t]\n"
        }
        data += "]"
        return data
    }

    private fun getNoteByIdWithIndex(idNote: Int): NoteWithIndex {
        for ((index, note) in notes.withIndex()) {
            if (idNote == note.id) return NoteWithIndex(index, note)
        }
        throw NoteNotFoundException("Заметка с индексом $idNote не найдена")
    }
}


fun main() {
    println(NoteService)
    var note1 = Note(title = "Заголовок заметки №1", text = "Содержание заметки #1")
    var note2 = Note(title = "Заголовок заметки №2", text = "Содержание заметки #2")
    var commentForNote1 = Comment(message = "Ахахахаха, верю!")
    var commentForNote1More = Comment(message = "Это всё какая-то дичь!!!")
    note1 = NoteService.add(note1)
    note2 = NoteService.add(note2)
    commentForNote1 = NoteService.createComment(idNote = note1.id, commentForNote1)
    commentForNote1More = NoteService.createComment(idNote = note1.id, commentForNote1More)
    println(NoteService)

    NoteService.deleteComment(note1.id, commentForNote1.id)
    NoteService.deleteComment(note1.id, commentForNote1.id)
    note2 = NoteService.delete(note2.id)
    println(NoteService)

    note1.title = "Новый заголовок для заметки №1"
    note1 = NoteService.edit(note1.id, note1)
    println(NoteService)

    commentForNote1More.message = "asd"
    NoteService.editComment(
        note1.id, commentForNote1More.id,
        commentForNote1More
    )
    println(NoteService)


}