import { useState, useEffect } from 'react';
import FlashCard from '../components/FlashCard';

const fetchMoreCardsUrl = "";
const submitScoreUrl = "";

export default function CramMode() {
    const [cards,setCards] = useState([]);
    const [currentIndex, setCurrentIndex] = useState(0);

    const fetchMoreCards = () => {
        fetch(fetchMoreCardsUrl)
        .then((response) => {
            return response.json();
        })
        .then((newCards) => {
            setCards((prevDeck) => [...prevDeck, ...newCards]);

        })
        .catch((error) => {
            console.error('Error fetching more cards:', error);
        });
    };

    function newCard() {
        if(currentIndex % 5 == 3) {
            fetchMoreCards();
        }
        setCurrentIndex(currentIndex+1);

    }

    const submitScore = (score) => {
        fetch(`${submitScoreUrl}/${id}/score`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ score: score })
        })
        .then(response => {
            if (!response.ok) console.error("Database didn't update the score");
        })
        .catch(err => console.error("Connection error", err));

        newCard();
    }

    useEffect(() => {
        const controller = new AbortController();
        fetchMoreCards();
        return () => controller.abort();
    }, []);

   if (cards.length === 0) {
        return <div className="text-center p-10">Loading deck...</div>;
    }

    const card = cards[currentIndex];
    const question = card.question;
    const answer = card.answer;
    const id = card.id;

    return (
        <FlashCard 
            key={currentIndex}
            question={question}
            answer={answer}
            newCard={submitScore}
        />
    )
}