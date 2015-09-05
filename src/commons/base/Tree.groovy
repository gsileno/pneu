package commons.base

public class Tree<T> {
    private List<Tree<T>> leaves = [];
    private Tree parent
    private T data

    public Tree(T data, Tree parent = null) {
        this.data = data
        this.parent = parent
    }
}