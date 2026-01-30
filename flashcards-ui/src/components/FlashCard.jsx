import { useState } from 'react';
import FeedBackControls from './FeedBackControls';

function BackSide({answer, onScore}) {
    return (
        <div>
            <h2>{answer}</h2>
            <FeedBackControls  onScore={onScore}/>
        </div>
    );
}


function FrontSide({question}) {
    return (
        <div>
            <h2>{question}</h2>
        </div>
    );
}

export default function FlashCard({ question, answer }) {
    const [isFlipped, setFlipped] = useState(false);

    function processFeedBack(score) {
        console.log("Score received:", score);
        setFlipped(false);
    }

    return (
        <div onClick={() => !isFlipped && setFlipped(true)}>
            {isFlipped ? (
                <BackSide answer={answer} onScore={processFeedBack} />
            ) : (
                <FrontSide question={question} />
            )}
        </div>
    );
}

