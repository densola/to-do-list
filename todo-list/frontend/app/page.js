"use client";

import { useState, useEffect } from "react";

export default function App() {
    const TASKS = [
        { name: "do task one", info: "more details provided here" },
        { name: "do task two at 3pm", info: "lorem ipsum dolor sit amet" },
        { name: "task three must be done", info: "yada yada yada" },
    ];

    // Get data
    const [tasks, setTasks] = useState([]);

    useEffect(() => {
        const searchUrl = "http://localhost:8000/getTasks";

        const fetchData = async () => {
            const response = await fetch(searchUrl);
            const data = await response.json();
            setTasks(data.tasks);
        };

        fetchData();
    }, []);

    if (tasks.length == 0) {
        return <h1><center>Loading...</center></h1>;
    } else {
        return (
            <div id="root">
                {/* TODO - don't hardcode url*/}
                {/* <form action="http://localhost:8000/" method="post">
                <input name="txt" type="text" />
                <button type="submit">Submit</button>
                </form> */}
                <button className="btn-add">Add task</button>
                <TaskList tasks={tasks}></TaskList>
            </div>
        );
    }
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
