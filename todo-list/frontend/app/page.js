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

    const [showModal, setShowModal] = useState(false);
    function handleModalClose() {
        setShowModal(false);
    }
    function handleModalOpen() {
        setShowModal(true);
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
            <h1>
                <center>Loading...</center>
            </h1>
        );
    }

    return (
        <div id="root">
            {/* TODO - don't hardcode url*/}
            <button className="btn-add" onClick={handleModalOpen}>
                Add task
            </button>
            <TaskList tasks={tasks}></TaskList>
            <Modal isOpen={showModal} doOnClose={handleModalClose}>
                <form
                    ref={ref}
                    action="http://localhost:8000/createTask"
                    method="post"
                    onSubmit={handleSubmit}
                >
                    <fieldset>
                        <label htmlFor="name">Task:</label>
                        <input name="name" type="text" maxLength={64} autoFocus />
                    </fieldset>
                    <fieldset>
                        <label htmlFor="info">More task details:</label>
                        <input name="info" type="text" />
                    </fieldset>
                    <button type="submit">Submit</button>
                </form>
            </Modal>
        </div>
    );
}

function Modal({ isOpen, doOnClose, children }) {
    if (!isOpen) return null;

    return (
        <div className="modal">
            <div className="modal__content">
                {children}
                <span className="modal__close" onClick={doOnClose}>
                    &times;
                </span>
            </div>
        </div>
    );
}

function TaskList({ tasks }) {
    const rows = [];

    let i = 0;
    tasks.forEach((task) => {
        rows.push(<Task task={task} key={i} />);
        i++;
    });

    return <div>{rows}</div>;
}

function Task({ task }) {
    return (
        <div className="task">
            <div className="task__header">
                <input className="task__checkbox" type="checkbox" />
                <h5 className="task__title">{task.name}</h5>
            </div>
            <p className="task__desc">{task.info}</p>
        </div>
    );
}
