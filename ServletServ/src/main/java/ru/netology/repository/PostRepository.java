package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {

    private final ConcurrentHashMap<Long, Post> _myPostStorage = new ConcurrentHashMap<>();
    static AtomicLong _postId = new AtomicLong(0);

    public List<Post> all() {
        if (!_myPostStorage.isEmpty()) //Возвращаем все имеющиеся посты:
            return new ArrayList<>(_myPostStorage.values());
        else
            throw new NullPointerException("There isn't post in repository!");
    }

    public Optional<Post> getById(long id)//Возвращаем пост по ID если есть:
    {
        return Optional.ofNullable(_myPostStorage.get(id));
    }

    public Post save(Post post) throws NotFoundException {

        if (post.getId() == 0) //ID поста равен нулю. Сохраняем новый пост:
        {
            post.setId(_postId.get());
            _postId.getAndIncrement();
            _myPostStorage.put(post.getId(), post);
        }
        else if (_myPostStorage.containsKey(post.getId())) //Уже есть пост с таким ID, обновляем его:
        {
            _myPostStorage.put(post.getId(), post);
        }
        else
            throw new NotFoundException("The post doesn't found");

        return post;
    }

    public void removeById(long id) throws NotFoundException
    {
        if (_myPostStorage.containsKey(id))
        {
            _myPostStorage.remove(id);
        }
        else
            throw new NotFoundException("The post doesn't found");
    }
}
