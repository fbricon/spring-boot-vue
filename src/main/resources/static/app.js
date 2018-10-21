const client = axios.create({
    baseURL: window.location.origin+'/api',
    json: true
});

async function execute(method, resource, data) {
    // inject the accessToken for each request
    return client({
        method,
        url: resource,
        data,
    }).then(req => {
        return req.data
    })
}

window.onload = function () {
    // visibility filters
    var filters = {
        all: function (todos) {
            return todos
        },
        active: function (todos) {
            return todos.filter(function (todo) {
                return !todo.completed
            })
        },
        completed: function (todos) {
            return todos.filter(function (todo) {
                return todo.completed
            })
        }
    }

    // app Vue instance
    var app = new Vue({
        // app initial state
        data: {
            todos: [],
            newTodo: '',
            editedTodo: null,
            visibility: 'all',
            loading: true,
        },
        async mounted() {
            await this.reload();
        },

        // watch todos change for localStorage persistence
        watch: {
            todos: {
                handler: async function (todos) {
                    if (this.loading) {
                        return;
                    }
                    console.log("todos changed, saving them");
                    //await todoService.updateTodo(todo);
                },
                deep: true
            }
        },

        // computed properties
        // http://vuejs.org/guide/computed.html
        computed: {
            filteredTodos: function () {
                return filters[this.visibility](this.todos)
            },
            remaining: function () {
                return filters.active(this.todos).length
            },
            allDone: {
                get: function () {
                    return this.remaining === 0
                },
                set: function (value) {
                    this.todos.forEach(function (todo) {
                        todo.completed = value
                    })
                }
            }
        },

        filters: {
            pluralize: function (n) {
                return n === 1 ? 'item' : 'items'
            }
        },

        // methods that implement data logic.
        // note there's no DOM manipulation here at all.
        methods: {

            reload: async function () {
                this.todos = await todoService.getTodos();
            },

            addTodo: async function () {
                var value = this.newTodo && this.newTodo.trim()
                if (!value) {
                    return
                }
                await todoService.createTodo({
                    title: value,
                    completed: false
                });
                await this.reload();
                this.newTodo = ''
            },

            toggle: async function (todo) {
                todo.completed = !todo.completed;
                await todoService.updateTodo(todo);
                await this.reload();
            },

            removeTodo: async function (todo) {
                await todoService.deleteTodo(todo);
                await this.reload();
            },

            editTodo: async function (todo) {
                this.beforeEditCache = todo.title
                this.editedTodo = todo
            },

            doneEdit: async function (todo) {
                if (!this.editedTodo) {
                    return
                }
                this.editedTodo = null
                todo.title = todo.title.trim();
                await todoService.updateTodo(todo);
                await this.reload();
            },

            cancelEdit: function (todo) {
                this.editedTodo = null
                todo.title = this.beforeEditCache
            },

            removeCompleted: async function () {
                await todoService.removeCompleted();
                this.reload();
            },

            resetData: async function(){
                this.todos = await todoService.resetData();
            }
        },

        // a custom directive to wait for the DOM to be updated
        // before focusing on the input field.
        // http://vuejs.org/guide/custom-directive.html
        directives: {
            'todo-focus': function (el, binding) {
                if (binding.value) {
                    el.focus()
                }
            }
        }
    })

    var todoService = {
        getTodos: function () {
            return execute('get', '/todos');
        },

        getTodo: function (id) {
            return execute('get', `/todos/${id}`);
        },

        createTodo(todo) {
            return execute('post', '/todos', todo)
        },

        updateTodo(todo) {
            if (todo.title === '') {
                return this.deleteTodo(todo);
            }                
            return execute('put', `/todos/${todo.id}`, todo)
        },

        deleteTodo(todo) {
            return execute('delete', `/todos/${todo.id}`)
        },

        removeCompleted() {
            return execute('delete', `/todos/purge`)
        },

        resetData() {
            return execute('get', `todos/reset`);
        }
    }

    // handle routing
    function onHashChange() {
        var visibility = window.location.hash.replace(/#\/?/, '')
        if (filters[visibility]) {
            app.visibility = visibility
        } else {
            window.location.hash = ''
            app.visibility = 'all'
        }
    }

    window.addEventListener('hashchange', onHashChange)
    onHashChange()

    // mount
    app.$mount('.todoapp')
}