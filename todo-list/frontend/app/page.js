"use client";

import { useState, useEffect, useRef } from "react";

export default function App() {
    const [tasks, setTasks] = useState([]);
    const [doneFetch, setDoneFetch] = useState(false);
    useEffect(() => {
        const searchUrl = "http://localhost:8000/getTasks";

        const fetchData = async () => {
            const response = await fetch(searchUrl);
            const data = await response.json();
            setTasks(data.tasks);
            setDoneFetch(true);
        };

        fetchData();
    }, []);

    const [showCreateModal, setShowCreateModal] = useState(false);
    function handleCreateModalClose() {
        setShowCreateModal(false);
    }
    function handleCreateModalOpen() {
        setShowCreateModal(true);
    }

    const ref = useRef();
    function handleSubmit(e) {
        setTimeout(() => {
            e.preventDefault();
            ref.current.submit();
            location.reload();
        }, 100);
    }

    if (tasks.length == 0 && !doneFetch) {
        return (
            <h1 className="notice">
                <center>Loading...</center>
            </h1>
        );
    }

    return (
        <div id="root">
            {/* TODO - don't hardcode url*/}
            <button className="add-btn" onClick={handleCreateModalOpen}>
                Add task
            </button>
            <TaskList tasks={tasks}></TaskList>
            <Modal isOpen={showCreateModal} doOnClose={handleCreateModalClose}>
                <form
                    ref={ref}
                    action="http://localhost:8000/createTask"
                    method="post"
                    onSubmit={handleSubmit}
                >
                    <fieldset>
                        <label htmlFor="name">
                            <strong>Enter task:</strong>
                        </label>
                        <input
                            name="name"
                            type="text"
                            maxLength={64}
                            autoFocus
                        />
                    </fieldset>
                    <fieldset>
                        <label htmlFor="info">
                            <strong>Enter details:</strong>
                        </label>
                        <textarea name="info" type="text" />
                    </fieldset>
                    <button type="submit">Add</button>
                </form>
            </Modal>
        </div>
    );
}

function Modal({ isOpen, doOnClose, children }) {
    if (!isOpen) return null;

    return (
        <div className="modal">
            <div className="modal__content notice">
                {children}
                <span className="modal__close" onClick={doOnClose}>
                    &times;
                </span>
            </div>
        </div>
    );
}

function TaskList({ tasks }) {
    function handleDelete(e) {
        setTimeout(() => {
            e.preventDefault();
            location.reload();
        }, 100);
    }

    function handleEdit(e) {
        setTimeout(() => {
            e.preventDefault();
            location.reload();
        }, 100);
    }

    const [showEditModal, setShowEditModal] = useState(false);
    const [toEditName, setToEditName] = useState("");
    const [toEditInfo, setToEditInfo] = useState("");
    function handleEditModalClose() {
        setShowEditModal(false);
    }
    function handleEditModalOpen(e, name, info) {
        setToEditName(name);
        setToEditInfo(info);
        setShowEditModal(true);
    }

    const rows = [];

    let i = 0;
    tasks.forEach((task) => {
        rows.push(
            <Task
                task={task}
                handleDelete={handleDelete}
                showEdit={handleEditModalOpen}
                key={i}
            />
        );
        i++;
    });

    return (
        <div>
            {rows}
            <Modal isOpen={showEditModal} doOnClose={handleEditModalClose}>
                <form
                    action="http://localhost:8000/editTask"
                    method="post"
                    onSubmit={handleEdit}
                >
                    <input type="hidden" name="name" value={toEditName}></input>
                    <input type="hidden" name="info" value={toEditInfo}></input>
                    <fieldset>
                        <label htmlFor="newname">
                            <strong>Edit task:</strong>
                        </label>
                        <input
                            name="newname"
                            type="text"
                            maxLength={64}
                            defaultValue={toEditName}
                            autoFocus
                        />
                    </fieldset>
                    <fieldset>
                        <label htmlFor="newinfo">
                            <strong>Edit details:</strong>
                        </label>
                        <textarea
                            name="newinfo"
                            type="textarea"
                            defaultValue={toEditInfo}
                        />
                    </fieldset>
                    <button type="submit">Done</button>
                </form>
            </Modal>
        </div>
    );
}

function Task({ task, handleDelete, showEdit }) {
    return (
        <form
            action="http://localhost:8000/deleteTask"
            method="post"
            onSubmit={handleDelete}
        >
            <input type="hidden" value={task.name} name="name"></input>
            <input type="hidden" value={task.info} name="info"></input>
            <div className="task notice">
                <div className="task__data">
                    <h5 className="task__name">
                        <span
                            class="material-symbols-outlined task__edit-btn"
                            onClick={(e) => showEdit(e, task.name, task.info)}
                        >
                            edit
                        </span>
                        <span>{task.name}</span>
                    </h5>
                    <span className="task__info">{task.info}</span>
                </div>
                <button type="submit">
                    <span class="material-symbols-outlined">delete</span>
                </button>
            </div>
        </form>
    );
}
